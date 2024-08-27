package com.example.tm.service;

import com.example.tm.dto.CommentMapper;
import com.example.tm.dto.TaskMapper;
import com.example.tm.entity.*;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.CommentRepository;
import com.example.tm.repository.TaskRepository;
import com.example.tm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserService userService;
    CommentMapper commentMapper = new CommentMapper(userRepository, taskRepository);
    TaskMapper taskMapper = new TaskMapper(userRepository);
    @InjectMocks
    TaskService taskService;

    private User getUserForTest() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("lsplay@yandex.ru");
        user.setPassword("qweqwe");
        return user;
    }

    public Task getTaskForTest() {
        User user = getUserForTest();
        user.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description des");
        task.setCreater(getUserForTest());
        task.setPriority(Priority.Low);
        task.setStatus(Status.InProcess);
        task.setExecutor(user);

        return task;
    }

    @Test
    public void getTaskByIdTest() throws NotFoundException {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.getTasksById(1L)).thenReturn(getTaskForTest());

        Assertions.assertEquals(taskMapper.toTaskDto(getTaskForTest()), taskService.getTaskById(1L));
    }

    @Test
    public void getTaskByIdExceptionTest() throws NotFoundException {
        when(taskRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {taskService.getTaskById(1L);});
    }

    @Test
    public void createTaskTitleExceptionTest() {
        Task task = getTaskForTest();
        task.setTitle("");
        when(userRepository.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(ValidationException.class, () -> {
            taskService.createTask(taskMapper.toTaskDto(task), "qweqwe");
        });
    }

    @Test
    public void createTaskDescriptionExceptionTest() {
        Task task = getTaskForTest();
        task.setDescription("");
        when(userRepository.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(ValidationException.class, () -> {
            taskService.createTask(taskMapper.toTaskDto(task), "qweqwe");
        });
    }

    @Test
    public void createTaskExecutorExceptionTest() {
        Task task = getTaskForTest();
        when(userRepository.existsById(2L)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> {
            taskService.createTask(taskMapper.toTaskDto(task), "qweqwe");
        });
    }

    @Test
    public void updateTaskByCreatorTest() throws ValidationException, NotFoundException {
        User executor = getUserForTest();
        executor.setId(2L);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.getTasksById(1l)).thenReturn(getTaskForTest());
        when(userService.getByToken("qweqwe")).thenReturn(getUserForTest());
        when(userRepository.getUserById(2L)).thenReturn(executor);

        Task taskForTest = getTaskForTest();
        taskForTest.setDescription("aaaaaa");

        Assertions.assertEquals(taskService.updateTask(taskMapper.toTaskDto(taskForTest),"qweqwe"),
                taskMapper.toTaskDto(taskForTest));

    }

    @Test
    public void updateTaskByExecutorTest() throws ValidationException, NotFoundException {
        User executor = getUserForTest();
        executor.setId(2L);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.getTasksById(1l)).thenReturn(getTaskForTest());
        when(userService.getByToken("qweqwe")).thenReturn(executor);

        Task taskForTest = getTaskForTest();
        taskForTest.setDescription("aaaaaa");
        taskForTest.setStatus(Status.Completed);

        Assertions.assertNotEquals(taskService.updateTask(taskMapper.toTaskDto(taskForTest),"qweqwe"),
                taskMapper.toTaskDto(taskForTest));
        Assertions.assertEquals(Status.Completed, taskForTest.getStatus());
    }

    @Test
    public void deleteTaskExceptionTest() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {taskService.deleteTask(1L);});
    }

    @Test
    public void getTaskByUserIdTest() throws NotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findTasksByCreaterId(1L)).thenReturn(List.of(getTaskForTest(), getTaskForTest()));

        Assertions.assertEquals(2, taskService.getTasksByUserId(1L).size());
    }

    @Test
    public void getTasksByUserIdWithCommentsTest() throws NotFoundException {
        PageRequest pageRequest = PageRequest.of(0,3);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findTaskByCreaterId(1L, pageRequest)).thenReturn(List.of(getTaskForTest(), getTaskForTest()));
        when(commentRepository.findCommentsByTaskId(1L, pageRequest)).thenReturn(new ArrayList<>());

        Assertions.assertEquals(2, taskService.getTasksByUserIdWithComments(1L, pageRequest, pageRequest).size());
    }

    @Test
    public void getTasksByUserIdWith2CommentsTest() throws NotFoundException {
        PageRequest pageRequest = PageRequest.of(0,3);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTask(getTaskForTest());
        comment.setCreater(getUserForTest());
        comment.setContent("Content");
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findTaskByCreaterId(1L, pageRequest)).thenReturn(List.of(getTaskForTest(), getTaskForTest()));
        when(commentRepository.findCommentsByTaskId(1L, pageRequest)).thenReturn(List.of(comment));

       Assertions.assertEquals(comment.getContent(), taskService.getTasksByUserIdWithComments(1L, pageRequest,
               pageRequest).get(0).getCommentPostDtos().get(0).getContent());
    }
}
