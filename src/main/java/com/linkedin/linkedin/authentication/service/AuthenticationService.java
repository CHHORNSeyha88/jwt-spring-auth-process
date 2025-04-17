package com.linkedin.linkedin.authentication.service;

import com.linkedin.linkedin.authentication.dto.response.LoginResponse;
import com.linkedin.linkedin.authentication.dto.req.LoginRequest;
import com.linkedin.linkedin.authentication.dto.response.RegisterResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService{
    RegisterResponse register(LoginRequest request) throws MessagingException, UnsupportedEncodingException;
    LoginResponse login(LoginRequest loginRequest);
    AuthenticationUser getUserByEmail(String email);
     void sendMailVerificationToken(String email);
     void validateEmailVerificationToken(String token, String email);
    void resetNewPassword(String email, String newPassword, String token) throws IllegalWriteException;
    void sendPasswordResetToken(String email);
}
