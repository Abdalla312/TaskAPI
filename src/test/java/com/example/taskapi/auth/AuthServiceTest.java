package com.example.taskapi.auth;

import com.example.taskapi.auth.dto.AuthRequest;
import com.example.taskapi.auth.dto.AuthResponse;
import com.example.taskapi.auth.dto.RefreshRequest;
import com.example.taskapi.auth.dto.RegisterRequest;
import com.example.taskapi.common.exception.UnauthorizedException;
import com.example.taskapi.security.CustomUserDetails;
import com.example.taskapi.security.JwtService;
import com.example.taskapi.user.Role;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserMapper;
import com.example.taskapi.user.UserService;
import com.example.taskapi.user.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void should_register_successfully() {
        // Build a RegisterRequest that mirrors the controller input.
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setUsername("test_user");
        registerRequest.setPassword("Passw0ordA");

        // Create a UserRequest (what the mapper should produce).
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setUsername("test_user");
        userRequest.setPassword("Passw0rdA");

        // Create a saved User (what userService.saveNewUser(...) returns).
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("test_user");
        savedUser.setEmail("test@example.com");
        savedUser.setRole(Role.USER);

        // Create a RefreshToken returned by refreshTokenService.createRefreshToken(savedUser). Stubs
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(savedUser);

        when(userMapper.toUserRequest(registerRequest)).thenReturn(userRequest);
        when(userService.saveNewUser(userRequest)).thenReturn(savedUser);
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(savedUser)).thenReturn(refreshToken);
        // Act
        AuthResponse response = authService.register(registerRequest);
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUsername()).isEqualTo("test_user");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");

        verify(userMapper).toUserRequest(registerRequest);
        verify(userService).saveNewUser(userRequest);
        verify(refreshTokenService).createRefreshToken(savedUser);
    }

    @Test
    void should_login_successfully() {
        // Arrange
        AuthRequest authReq = new AuthRequest();
        authReq.setIdentifier("test_user");
        authReq.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        when(jwtService.generateToken(userDetails)).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);

        // Act
        AuthResponse response = authService.login(authReq);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUsername()).isEqualTo("test_user");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");

        verify(authenticationManager).authenticate(any());
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void should_refresh_token_successfully() {
        // Arrange
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("refresh-token");

        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        RefreshToken verifiedToken = new RefreshToken();
        verifiedToken.setUser(user);
        verifiedToken.setToken("refresh-token");

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(user);
        newToken.setToken("new-refresh-token");

        when(refreshTokenService.verifyToken("refresh-token")).thenReturn(verifiedToken);
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("new-access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(newToken);

        // Act
        AuthResponse response = authService.refresh(request);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.getUsername()).isEqualTo("test_user");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");

        verify(refreshTokenService).verifyToken("refresh-token");
        verify(refreshTokenService).revokeToken("refresh-token");
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void should_fail_refresh_when_token_invalid() {
        // Arrange
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("refresh-token");

        when(refreshTokenService.verifyToken("refresh-token"))
                .thenThrow(new UnauthorizedException("Invalid refresh token"));

        // Act + Assert
        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid refresh token");

        verify(refreshTokenService, never()).revokeToken(anyString());
        verify(refreshTokenService, never()).createRefreshToken(any(User.class));
    }
}
