package com.example.taskapi.user;

import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.common.exception.UserAlreadyExistsException;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.user.dto.UserRequest;
import com.example.taskapi.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest dto) {
        log.info("Create user requested: username={}", dto.getUsername());
        return userMapper.toDTO(saveNewUser(dto));
    }

    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {

        log.debug("Fetching all users requested.");
        return PageResponse.from(
                userRepository.findAll(pageable)
                        .map(userMapper::toDTO));
    }

    public UserResponse getUserById(Long id) {
        log.debug("Fetch user with id={}", id);
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.debug("Update user from {} to {} ", user, userMapper.toEntity(dto));

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

    public UserResponse setAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid user Id"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.info("Updated user: {} role to ADMIN", id);
        return userMapper.toDTO(user);
    }
}
