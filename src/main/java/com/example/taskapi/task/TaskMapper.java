package com.example.taskapi.task;

import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "taskStatus", target = "status")
    Task toEntity(TaskRequest dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source ="user.username", target = "username")
    TaskResponse toDTO(Task task);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "taskStatus", target = "status")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(TaskRequest taskRequest, @MappingTarget Task task);
}
