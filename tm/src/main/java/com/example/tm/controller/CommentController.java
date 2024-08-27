package com.example.tm.controller;

import com.example.tm.dto.CommentDto;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @GetMapping(value = "/comment/{id}")
    public CommentDto getComment(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на получение комментария");
        return commentService.getCommentById(id);
    }

    @PostMapping(value = "/comment/")
    public CommentDto getComment(@RequestBody CommentDto commentDto,
                                 @RequestHeader (value = "Authorization") String token)
            throws ValidationException, NotFoundException {
        log.info("Поступил запрос на создание комментария:" + commentDto);
        commentService.createComment(commentDto, token);
        log.info("Комментарий был создан:" + commentDto);
        CommentDto commentDto1 = commentService.getLastComment();
        return commentDto1;
    }


}
