package com.example.taskapi.user;

import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.common.exception.UserAlreadyExistsException;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.user.dto.UserRequest;
import com.example.taskapi.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest dto) {
        return userMapper.toDTO(saveNewUser(dto));
    }

    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        return PageResponse.from(
                userRepository.findAll(pageable)
                        .map(userMapper::toDTO));
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateEntityFromDto(dto, user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.deleteById(id);
    }

    public User saveNewUser(UserRequest dto) {
        // from DTO Request -> Entity
        User user = userMapper.toEntity(dto);
        // check username & email should be unique
        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        // save into Entity

        return userRepository.save(user);
    }
}
