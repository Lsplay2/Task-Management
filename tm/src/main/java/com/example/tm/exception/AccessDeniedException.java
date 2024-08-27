package com.example.tm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedException extends Exception {
    public AccessDeniedException(String e) {
        super(e);
    }
}
