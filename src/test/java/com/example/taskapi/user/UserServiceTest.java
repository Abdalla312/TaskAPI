package com.example.taskapi.user;

import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.common.exception.UserAlreadyExistsException;
import com.example.taskapi.user.dto.UserRequest;
import com.example.taskapi.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void should_ReturnUser_WhenUserExists() {
        User user = new User(1L, Role.USER, "Test_user", "email@test.com", "testSecret_123", null, LocalDateTime.now(), LocalDateTime.now());
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setEmail("email@test.com");
        response.setUsername("Test_user");
        response.setRole("USER");

        when(userMapper.toDTO(user)).thenReturn(response);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse result = userService.getUserById(1L);
        assertEquals("Test_user", result.getUsername());
    }

    @Test
    void should_createUser_successfully() {
        UserRequest dto = new UserRequest();
        dto.setEmail("user@test.com");
        dto.setUsername("TestUser");
        dto.setPassword("Passw0rdA");

        User mapped = new User();
        mapped.setEmail(dto.getEmail());
        mapped.setUsername(dto.getUsername());
        mapped.setPassword(dto.getPassword());

        User saved = new User();
        saved.setId(1L);
        saved.setUsername(mapped.getUsername());
        saved.setEmail(mapped.getEmail());
        saved.setRole(Role.USER);

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setEmail(dto.getEmail());
        response.setUsername(dto.getUsername());
        response.setRole("USER");

        when(userMapper.toEntity(dto)).thenReturn(mapped);
        when(userRepository.existsUserByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByUsername(dto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded");
        when(userRepository.save(mapped)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(response);

        UserResponse result = userService.createUser(dto);

        assertEquals("TestUser", result.getUsername());
        assertEquals("USER", result.getRole());
        verify(passwordEncoder).encode("Passw0rdA");
        verify(userRepository).save(mapped);

    }

    @Test
    void should_throwsNotFound_when_Missing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void should_throwConflict_when_duplicateEmail() {
        UserRequest dto = new UserRequest();
        dto.setEmail("dup@test.com");
        dto.setUsername("TestUser");
        dto.setPassword("Passw0rdA");

        User mapped = new User();
        mapped.setEmail(dto.getEmail());
        mapped.setUsername(dto.getUsername());
        mapped.setPassword(dto.getPassword());

        when(userMapper.toEntity(dto)).thenReturn(mapped);
        when(userRepository.existsUserByEmail("dup@test.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void should_throwConflict_when_duplicateUsername() {
        UserRequest dto = new UserRequest();
        dto.setEmail("dup@test.com");
        dto.setUsername("TestDupUser");
        dto.setPassword("Passw0rdA");

        User mapped = new User();
        mapped.setEmail(dto.getEmail());
        mapped.setUsername(dto.getUsername());
        mapped.setPassword(dto.getPassword());

        when(userMapper.toEntity(dto)).thenReturn(mapped);
        when(userRepository.existsUserByUsername("TestDupUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void should_getUserById_successfully() {

        User user = new User(1L, Role.USER, "TestUser", "user@test.com", "Passw0rdA", null, null, null);

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setEmail("user@test.com");
        response.setUsername("TestUser");
        response.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(response);

        UserResponse result = userService.getUserById(1L);

        assertEquals("TestUser", result.getUsername());
        assertEquals("user@test.com", result.getEmail());

    }

}
