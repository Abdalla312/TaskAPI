package com.example.taskapi.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private String timestamp;

}
