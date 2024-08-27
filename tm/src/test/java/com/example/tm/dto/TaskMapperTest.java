package com.example.tm.dto;

import com.example.tm.entity.Priority;
import com.example.tm.entity.Status;
import com.example.tm.entity.Task;
import com.example.tm.entity.User;
import com.example.tm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TaskMapper taskMapper;

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

    public TaskDto getTaskDtoForTest() {
        TaskDto taskDto = new TaskDto
                (1L,"Title", "Description des", "InProcess", "Low", 1L, 2L);
        return taskDto;
    }
    @Test
    public void fromTaskDtoTest() {
        User executor = getUserForTest();
        executor.setId(2L);

        Assertions.assertEquals(getTaskDtoForTest(), taskMapper.toTaskDto(getTaskForTest()));
    }

    @Test
    public void toTaskDtoTest() {
        Assertions.assertEquals(getTaskDtoForTest(), taskMapper.toTaskDto(getTaskForTest()));
    }

    @Test
    public void toTaskDtoList() {
        Assertions.assertEquals(List.of(getTaskDtoForTest(),getTaskDtoForTest()),
                taskMapper.toTaskDtoList(List.of(getTaskForTest(), getTaskForTest())));
    }
}
