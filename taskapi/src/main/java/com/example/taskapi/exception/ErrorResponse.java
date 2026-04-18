package com.example.taskapi.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private String timestamp;

}
