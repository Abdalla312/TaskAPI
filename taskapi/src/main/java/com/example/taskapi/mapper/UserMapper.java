package com.example.taskapi.mapper;

import com.example.taskapi.dto.UserRequest;
import com.example.taskapi.dto.UserResponse;
import com.example.taskapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest dto);

    UserResponse toDTO(User user);
    List<UserResponse> toDTO(List<User> users);
    void updateEntityFromDto(UserRequest dto, @MappingTarget User entity);
}
