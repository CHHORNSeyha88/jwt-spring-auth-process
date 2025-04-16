package com.linkedin.linkedin.authentication.service;

import com.linkedin.linkedin.authentication.dto.AuthenticationRequest;
import com.linkedin.linkedin.authentication.dto.AuthenticationResponse;
import com.linkedin.linkedin.authentication.dto.LoginRequest;
import com.linkedin.linkedin.authentication.dto.RegisterResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService{
    RegisterResponse register(AuthenticationRequest request) throws MessagingException, UnsupportedEncodingException;
    AuthenticationResponse login(LoginRequest loginRequest);
    AuthenticationUser getUserByEmail(String email);
     void sendMailVerificationToken(String email);
     void validateEmailVerificationToken(String token, String email);
    void resetNewPassword(String email, String newPassword, String token) throws IllegalWriteException;
    void sendPasswordResetToken(String email);
}
