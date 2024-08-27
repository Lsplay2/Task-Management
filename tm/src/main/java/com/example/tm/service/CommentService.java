package com.example.tm.service;

import com.example.tm.dto.CommentDto;
import com.example.tm.dto.CommentMapper;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.CommentRepository;
import com.example.tm.repository.TaskRepository;
import com.example.tm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    TaskRepository taskRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    UserService userService;

    @Autowired
    CommentService(TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository,
                   UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.commentMapper = new CommentMapper(userRepository, taskRepository);
    }

    public CommentDto getCommentById(Long id) throws NotFoundException {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Комментарий не найден");
        }

        return commentMapper.toCommentDto(commentRepository.getCommentById(id));
    }

    public void createComment(CommentDto commentDto, String token) throws ValidationException, NotFoundException {
        validateCreateComment(commentDto);
        commentDto.setCreater(userService.getByToken(token).getId());
        commentRepository.save(commentMapper.fromCommentDto(commentDto));
    }

    public CommentDto getLastComment() {
        return commentMapper.toCommentDto(commentRepository.findFirstByOrderByIdDesc());
    }


    private void validateCreateComment(CommentDto commentDto) throws NotFoundException, ValidationException {
        validateCheckTask(commentDto.getTask());

        if (commentDto.getContent() == null || commentDto.getContent().isBlank()) {
            throw new ValidationException("Комментарий пуст");
        }
    }

    private void validateCheckUser(Long id) throws NotFoundException {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void validateCheckTask(Long id) throws NotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Задача не найдена");
        }
    }

}
