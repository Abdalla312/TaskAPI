package com.example.taskapi.common.exception;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String message) {super(message);}
}
