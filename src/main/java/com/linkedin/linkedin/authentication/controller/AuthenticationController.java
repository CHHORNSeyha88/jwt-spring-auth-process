package com.linkedin.linkedin.authentication.controller;

import com.linkedin.linkedin.authentication.dto.response.LoginResponse;
import com.linkedin.linkedin.authentication.dto.req.LoginRequest;
import com.linkedin.linkedin.authentication.dto.response.RegisterResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import com.linkedin.linkedin.authentication.service.AuthenticationServiceImpl;
import com.linkedin.linkedin.authentication.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final SecurityUtil securityUtil;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody LoginRequest request) throws MessagingException, UnsupportedEncodingException {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PutMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationToken(@RequestParam @Email String email) {
        authenticationService.sendMailVerificationToken(email);
        return ResponseEntity.ok("A new verification code has been sent to your email.");
    }


    @PutMapping("/send-password-reset-token")
    public String sendPasswordResetToken(@RequestParam String email) {
        authenticationService.sendPasswordResetToken(email);
        return "Password reset token sent successfully.";
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String newPassword, @RequestParam String token, @RequestParam String email) throws IllegalWriteException {
        authenticationService.resetNewPassword(email, newPassword, token);
        return "Password reset successfully.";
    }

    @PutMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam @Email String email,
            @RequestParam String token) {
        try {
            authenticationService.validateEmailVerificationToken(token, email);
            return ResponseEntity.ok("Email successfully verified.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<AuthenticationUser> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(authenticationService.getUserByEmail(email));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile")
    public AuthenticationUser updateUserProfile(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String location
    ) {
        Long userUpdateId = securityUtil.getCurrentUserId();
        return authenticationService.updateUserProfile(userUpdateId, firstName, lastName, company, position, location);

    }

}
