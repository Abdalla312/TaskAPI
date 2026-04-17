package com.example.taskapi.service;


import com.example.taskapi.dto.UserRequestDTO;
import com.example.taskapi.dto.UserResponseDTO;
import com.example.taskapi.mapper.UserMapper;
import com.example.taskapi.model.User;
import com.example.taskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        // from DTO Request -> Entity
        User user = userMapper.toEntity(dto);
        // save into Entity
        User savedUser = userRepository.save(user);
        // from Entity -> DTO response
        return userMapper.toDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userMapper.toDTO(userRepository.findAll());
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO updatedUserDTO) {
        Optional<User> oldUser = userRepository.findById(id);
        User updatedUser = userMapper.toEntity(updatedUserDTO);
        return Optional.of(userMapper.toDTO(userRepository.save(updatedUser)));
    }

    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
