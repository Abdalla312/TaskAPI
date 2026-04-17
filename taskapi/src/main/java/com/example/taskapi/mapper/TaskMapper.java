package com.example.taskapi.mapper;

import com.example.taskapi.dto.TaskResponseDTO;
import com.example.taskapi.dto.TaskRequestDTO;
import com.example.taskapi.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequestDTO dto);
    TaskResponseDTO toDTO(Task task);
}
