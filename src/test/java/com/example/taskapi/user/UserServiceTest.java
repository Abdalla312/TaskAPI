package com.example.taskapi.user;

import com.example.taskapi.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser_WhenUserExists() {
        User user = new User(1L, Role.USER, "Test_user", "email@test.com", "testSecret_123", null, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse result = userService.getUserById(1L);
        assertEquals("Test_user", result.getUsername());
    }

    @Test
    void should_createUser_successfully() {

    }

    @Test
    void should_throwConflict_when_duplicateEmail() {
    }

    @Test
    void should_throwConflict_when_duplicateUsername() {
    }

    @Test
    void should_getUserById_successfully() {
    }

    @Test
    void should_throwNotFound_when_userNotFound() {
    }


}
