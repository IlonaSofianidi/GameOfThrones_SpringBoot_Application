package com.example.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameCharacterNotFoundException extends RuntimeException {
    public GameCharacterNotFoundException(String ex) {
        super(ex);
    }
}
