package com.example.tm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends Exception {
    public ValidationException(String e) {
        super(e);
    }
}