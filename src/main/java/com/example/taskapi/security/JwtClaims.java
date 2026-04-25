package com.example.taskapi.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtClaims {
    private Long userId;
    private String role;

}
