package com.example.tm.dto;

import com.example.tm.entity.Comment;
import com.example.tm.repository.TaskRepository;
import com.example.tm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    UserRepository userRepository;
    TaskRepository taskRepository;

    @Autowired
    public CommentMapper(UserRepository userRepository, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment fromCommentDto(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setContent(commentDto.getContent());
        comment.setCreater(userRepository.getUserById(commentDto.getCreater()));
        comment.setTask(taskRepository.getTasksById(commentDto.getTask()));

        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .creater(comment.getCreater().getId())
                .task(comment.getTask().getId())
                .build();
    }

    public List<CommentDto> toCommentDtoList(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentDtoList.add(toCommentDto(comment));
        }

        return commentDtoList;
    }

    public CommentPostDto toCommentPostDto(Comment comment) {
        return CommentPostDto.builder()
                .content(comment.getContent())
                .creater(comment.getCreater().getName())
                .build();

    }

    public List<CommentPostDto> toListCommentPostDto(List<Comment> commentList) {
        List<CommentPostDto> commentPostDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentPostDtoList.add(toCommentPostDto(comment));
        }

        return commentPostDtoList;
    }

    public TaskCommentsDto toTaskCommentsDto(TaskDto taskDto, List<Comment> commentList) {
        return TaskCommentsDto.builder()
                .taskDto(taskDto)
                .commentPostDtos(toListCommentPostDto(commentList))
                .build();
    }
}
