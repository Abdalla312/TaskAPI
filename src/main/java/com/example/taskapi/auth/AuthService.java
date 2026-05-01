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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;


    @Transactional
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
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser());

        return new AuthResponse(jwtToken, refreshToken.getToken(), userDetails.getUsername(), role);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        UserRequest userRequest = userMapper.toUserRequest(request);
        User savedUser = userService.saveNewUser(userRequest);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String token = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(savedUser).getToken();
        return new AuthResponse(token, refreshToken, savedUser.getUsername(), role);
    }
    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        //verify token get user
        RefreshToken verifiedToken = refreshTokenService.verifyToken(request.getRefreshToken());
        User user = verifiedToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        // generate new token
        String newJwt = jwtService.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // revoke old then issue new one
        return new AuthResponse(newJwt, null, user.getUsername(), role);
    }
    @Transactional
    public void logout(RefreshRequest request){
        refreshTokenService.revokeToken(request.getRefreshToken());
    }
}
