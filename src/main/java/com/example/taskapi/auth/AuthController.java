package com.example.taskapi.auth;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.example.taskapi.user.dto.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //TODO: Add /register route mapping
//    @GetMapping("/register")
//    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @ValidUserRequest userRequest){
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(_,_));

    }

    //TODO: Configure /login route mapping
//    @GetMapping("/login")
//    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid UserDetails userDetails)

}
