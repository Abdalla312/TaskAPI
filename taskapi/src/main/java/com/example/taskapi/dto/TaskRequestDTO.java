package com.example.taskapi.dto;

import com.example.taskapi.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
    private String title;
    private String description;
    private TaskStatus taskStatus;
}
