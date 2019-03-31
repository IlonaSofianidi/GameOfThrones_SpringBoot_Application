package com.example.homework.controller;

import com.example.homework.exception.GameCharacterNotFoundException;
import com.example.homework.exception.RelationShipStoryNotFound;
import com.example.homework.wire.ErrorDetailsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class GameCharacterControllerAdvise extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GameCharacterNotFoundException.class)
    public final ResponseEntity<ErrorDetailsResponse> handleGameCharacterNotFoundException(GameCharacterNotFoundException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RelationShipStoryNotFound.class)
    public final ResponseEntity<ErrorDetailsResponse> handleRelationStoryNotFoundException(RelationShipStoryNotFound ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(new Date(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}