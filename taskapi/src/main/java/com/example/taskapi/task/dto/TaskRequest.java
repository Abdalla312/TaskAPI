package com.example.taskapi.task.dto;

import com.example.taskapi.common.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    @NotBlank(groups = OnCreate.class)
    private String title;
    @NotBlank(groups = OnCreate.class)
    private String description;

    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Status should be TODO, IN_PROGRESS, or DONE.")
    private String taskStatus;
    private Long userId;
}
