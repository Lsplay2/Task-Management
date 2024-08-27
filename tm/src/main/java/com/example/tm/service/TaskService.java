package com.example.tm.service;

import com.example.tm.dto.CommentMapper;
import com.example.tm.dto.TaskCommentsDto;
import com.example.tm.dto.TaskDto;
import com.example.tm.dto.TaskMapper;
import com.example.tm.entity.Priority;
import com.example.tm.entity.Status;
import com.example.tm.entity.Task;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.CommentRepository;
import com.example.tm.repository.TaskRepository;
import com.example.tm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    TaskMapper taskMapper;
    CommentMapper commentMapper;
    UserService userService;

    @Autowired
    TaskService(TaskRepository taskRepository, UserRepository userRepository, UserService userService,
                CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.taskMapper = new TaskMapper(userRepository);
        this.commentMapper = new CommentMapper(userRepository, taskRepository);
    }



    public TaskDto getTaskById(Long id) throws NotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Wrong id");
        }
        return taskMapper.toTaskDto(taskRepository.getTasksById(id));
    }

    public void createTask(TaskDto taskDto, String token) throws ValidationException, NotFoundException {
        validationCreateTask(taskDto);
        taskDto.setCreater(userService.getByToken(token).getId());
        taskRepository.save(taskMapper.fromTaskDto(taskDto));
    }

    public TaskDto updateTask(TaskDto taskDto, String token) throws ValidationException, NotFoundException {
        if (!taskRepository.existsById(taskDto.getId())) {
            throw new NotFoundException("Задача не найдена");
        }
        Task task = taskRepository.getTasksById(taskDto.getId());

        if (userService.getByToken(token).getId().equals(task.getCreater().getId())) {
            if (taskDto.getTitle() != null)
                task.setTitle(taskDto.getTitle());
            if (taskDto.getDescription() != null)
                task.setDescription(taskDto.getDescription());
            if (taskDto.getStatus() != null)
                task.setStatus(Status.valueOf(taskDto.getStatus()));
            if (taskDto.getExecutor() != null)
                task.setExecutor(userRepository.getUserById(taskDto.getExecutor()));
            if (taskDto.getPriority() != null)
                task.setPriority(Priority.valueOf(taskDto.getPriority()));

        } else if (userService.getByToken(token).getId().equals(task.getExecutor().getId())) {
            if (taskDto.getStatus() != null)
                task.setStatus(Status.valueOf(taskDto.getStatus()));
        }

        taskRepository.save(task);
        return taskMapper.toTaskDto(task);
    }

    public void deleteTask(Long id) throws NotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Задача не была найдена");
        }

        taskRepository.deleteById(id);
    }

    public List<TaskDto> getTasksByUserId (Long id) throws NotFoundException {
        validationCheckUser(id);

        return taskMapper.toTaskDtoList(taskRepository.findTasksByCreaterId(id));
    }

    public List<TaskCommentsDto> getTasksByUserIdWithComments (Long id, PageRequest taskPageRequest
            , PageRequest comPageRequest) throws NotFoundException {
        validationCheckUser(id);
        List<Task> taskList = taskRepository.findTaskByCreaterId(id, taskPageRequest.previousOrFirst());
        List<TaskCommentsDto> taskCommentsDtos = new ArrayList<>();
        System.out.println(taskList);
        for (Task task : taskList) {
            taskCommentsDtos.add(commentMapper.toTaskCommentsDto
                    (taskMapper.toTaskDto(task), commentRepository.findCommentsByTaskId(task.getId(), comPageRequest.previousOrFirst())));
        }
        return taskCommentsDtos;
    }

    public TaskDto getLastTask() {
        return taskMapper.toTaskDto(taskRepository.findFirstByOrderByIdDesc());
    }

    private void validationCreateTask(TaskDto taskDto) throws NotFoundException, ValidationException {
        validationCheckUser(taskDto.getExecutor());
        validationCheckStatusOrPriority(taskDto.getStatus(), taskDto.getPriority());
        if (taskDto.getTitle() == null || taskDto.getTitle().isBlank()
                || taskDto.getDescription() == null || taskDto.getDescription().isBlank()) {
            throw new ValidationException("Данные не заполнены");
        }
    }

    private void validationCheckUser(Long user) throws NotFoundException {
        if (!userRepository.existsById(user)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void validationCheckStatusOrPriority(String status, String priority) throws ValidationException {
        try {
            Status.valueOf(status);
            Priority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Указан неверный статус или приоритет");
        }
    }
}
