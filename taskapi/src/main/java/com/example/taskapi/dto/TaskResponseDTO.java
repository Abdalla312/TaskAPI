package com.example.taskapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime updatedAt;
}
