package com.example.taskapi.task;

import com.example.taskapi.task.dto.TaskResponse;
import com.example.taskapi.task.dto.TaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequest dto);
    TaskResponse toDTO(Task task);
    void updateEntityFromDto(TaskRequest taskRequest, @MappingTarget Task task);
}
