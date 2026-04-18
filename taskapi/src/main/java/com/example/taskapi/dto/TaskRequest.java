package com.example.taskapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String taskStatus;
    private Long user_id;
}
