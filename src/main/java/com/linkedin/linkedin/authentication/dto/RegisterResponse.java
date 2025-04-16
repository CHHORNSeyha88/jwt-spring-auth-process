package com.linkedin.linkedin.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String email;
    private String message="Please verify the code that has been sent to your email.";
}
