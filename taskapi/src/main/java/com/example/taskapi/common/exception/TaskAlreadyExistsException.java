package com.example.taskapi.common.exception;

public class TaskAlreadyExistsException extends ConflictException{ 
    public TaskAlreadyExistsException(String message){super(message);}
}
