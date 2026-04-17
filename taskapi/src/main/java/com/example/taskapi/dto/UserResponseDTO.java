package com.example.taskapi.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;
}
