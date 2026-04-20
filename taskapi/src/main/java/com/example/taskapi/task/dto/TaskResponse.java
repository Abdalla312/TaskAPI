package com.example.taskapi.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String taskStatus;
    private Long userId;
    private String username;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}
