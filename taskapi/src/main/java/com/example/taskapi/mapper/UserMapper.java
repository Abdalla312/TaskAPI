package com.example.taskapi.mapper;

import com.example.taskapi.dto.UserRequestDTO;
import com.example.taskapi.dto.UserResponseDTO;
import com.example.taskapi.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toDTO(User user);
    List<UserResponseDTO> toDTO(List<User> users);
}
