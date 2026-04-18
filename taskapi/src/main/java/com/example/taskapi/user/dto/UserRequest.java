package com.example.taskapi.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @Min(value = 4, message = "Username should be longer 4 character.")
    @Max(value = 15, message = "Username should be shorter than 20 character.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;
    //a simple regex pattern that ensures to have digits (0-9) , characters [a-z], [A-Z] ,and have a length of 8 or more.
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$")
    private String password;
}
