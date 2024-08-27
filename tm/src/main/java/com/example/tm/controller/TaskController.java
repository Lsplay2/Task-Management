package com.example.tm.controller;

import com.example.tm.dto.TaskCommentsDto;
import com.example.tm.dto.TaskDto;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @GetMapping(value = "/task/{id}")
    public TaskDto getTaskById(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на получение задачи по id:" + id);
        return taskService.getTaskById(id);
    }

    @GetMapping(value = "/task/user/{id}")
    public List<TaskDto> getTasksByUserId(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на получение задач пользователя:" + id);
        return taskService.getTasksByUserId(id);
    }

    @GetMapping(value = "/task/user/{id}/comments/")
    public List<TaskCommentsDto> getTasksByUserIdWithComments(
            @PathVariable Long id,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "20", required = false) int limit,
            @RequestParam(value = "offsetCom", defaultValue = "0", required = false) int offsetCom,
            @RequestParam(value = "limitCom", defaultValue = "20", required = false) int limitCom) throws NotFoundException {
        log.info("Поступил запрос на получение задач пользователя c комментариями:" + id);
        return taskService.getTasksByUserIdWithComments(id, PageRequest.of(offset, limit), PageRequest.of(offsetCom, limitCom));
    }

    @PostMapping(value = "/task/")
    public TaskDto createTask(@RequestBody TaskDto taskDto, @RequestHeader (value = "Authorization") String token) throws ValidationException, NotFoundException {
        log.info("Поступил запрос на создание задачи:" + taskDto);
        taskService.createTask(taskDto, token);
        TaskDto task = taskService.getLastTask();
        log.info("Задача добавлена в коллекцию:" + task);
        return task;
    }

    @PatchMapping(value = "/task/")
    public TaskDto updateTask(@RequestBody TaskDto taskDto, @RequestHeader (value = "Authorization") String token)
            throws NotFoundException, ValidationException {
        taskService.getTaskById(taskDto.getId());
        log.info("Поступил запрос на обновлении задачи" + taskService.getTaskById(taskDto.getId()));
        TaskDto taskDto1 = taskService.updateTask(taskDto, token);
        log.info("Задача была обновлена:" + taskDto1);
        return taskDto1;
    }

    @DeleteMapping(value = "/task/{id}")
    public String deleteTask(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на удаление задачи c id:" + id);
        taskService.deleteTask(id);
        log.info("Задача с id " + id + " была удалена");
        return "Успешно";
    }

}
