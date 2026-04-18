package com.example.taskapi.mapper;

import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequest dto);
    TaskResponse toDTO(Task task);
    void updateEntityFromDto(TaskRequest taskRequest, @MappingTarget Task task);
}
