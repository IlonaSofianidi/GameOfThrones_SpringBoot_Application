package com.example.homework.wire;

import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class ErrorDetailsResponse {
    private Date timestamp;
    private String message;
    private String details;

}
