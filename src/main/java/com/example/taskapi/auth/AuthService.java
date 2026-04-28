package com.example.taskapi.auth;

import com.example.taskapi.security.CustomUserDetails;
import com.example.taskapi.security.JwtService;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserMapper;
import com.example.taskapi.user.UserService;
import com.example.taskapi.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;


    public AuthResponse login(AuthRequest authRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getIdentifier(),
                        authRequest.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken, userDetails.getUsername(), role);
    }

    public AuthResponse register(RegisterRequest request) {

        UserRequest userRequest = userMapper.toUserRequest(request);
        User savedUser = userService.saveNewUser(userRequest);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getUsername(), role);
    }
}
