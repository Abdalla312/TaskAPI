package com.example.taskapi.common.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){super(message);}
}
