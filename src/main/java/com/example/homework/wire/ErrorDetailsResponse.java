package com.example.homework.wire;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorDetailsResponse {
    private Date timestamp;
    private String message;
    private String details;

}
