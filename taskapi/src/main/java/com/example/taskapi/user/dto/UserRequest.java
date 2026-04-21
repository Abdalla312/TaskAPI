package com.example.taskapi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @Size(min = 5, max = 15, message = "Invalid Username should be at least 5-15 characters long.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;
    //a simple regex pattern that ensures to have digits (0-9) , characters [a-z], [A-Z] ,and have a length of 8 or more.
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "weak password should be at least 12–15 characters long, mixing uppercase, lowercase, numbers, and symbols ")
    private String password;
}
