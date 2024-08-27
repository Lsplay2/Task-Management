package com.example.tm.dto;

import com.example.tm.entity.Priority;
import com.example.tm.entity.Status;
import com.example.tm.entity.Task;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TaskMapper {
    UserRepository userRepository;

    @Autowired
    public TaskMapper(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Task fromTaskDto(TaskDto taskDto) throws ValidationException {

         try {
             Status.valueOf(taskDto.getStatus());
             Priority.valueOf(taskDto.getPriority());
         } catch (IllegalArgumentException e) {
             throw new ValidationException("Указан не верный статус или приоритет");
         }
         Task task = new Task();
         task.setStatus(Status.valueOf(taskDto.getStatus()));
         task.setPriority(Priority.valueOf(taskDto.getPriority()));
         task.setCreater(userRepository.getUserById(taskDto.creater));
         task.setExecutor(userRepository.getUserById(taskDto.executor));
         task.setTitle(taskDto.getTitle());
         task.setDescription(taskDto.getDescription());
         return task;
    }

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().toString())
                .priority(task.getPriority().toString())
                .executor(task.getExecutor().getId())
                .creater(task.getCreater().getId())
                .build();
    }

    public List<TaskDto> toTaskDtoList(List<Task> tasks) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : tasks) {
            taskDtoList.add(toTaskDto(task));
        }

        return taskDtoList;
    }
}
