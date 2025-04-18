package com.linkedin.linkedin.authentication.service;

import com.linkedin.linkedin.authentication.dto.req.LoginRequest;
import com.linkedin.linkedin.authentication.dto.response.LoginResponse;
import com.linkedin.linkedin.authentication.dto.response.RegisterResponse;
import com.linkedin.linkedin.authentication.jwt.JwtUtils;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import com.linkedin.linkedin.authentication.repository.AuthenticationRepository;
import com.linkedin.linkedin.authentication.utils.EmailSender;
import com.linkedin.linkedin.authentication.utils.SecurityUtil;
import com.linkedin.linkedin.exceptioncontroller.EmailNotVerifiedException;
import com.linkedin.linkedin.exceptioncontroller.InvalidCredentialsException;
import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final int durationInMinutes = 1;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    //    generate
    public String generateEmailVerification() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder token = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            token.append(secureRandom.nextInt(10));
        }
        return token.toString();
    }

    //send email verification
    @Override
    public void sendMailVerificationToken(String email) {
        Optional<AuthenticationUser> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with this email does not exist.");
        }

        AuthenticationUser user = userOpt.get();

        if (user.getEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified.");
        }

        String emailVerificationCode = generateEmailVerification(); // e.g., 6-digit code
        String hashedToken = passwordEncoder.encode(emailVerificationCode);
        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationExpirationDate(LocalDateTime.now().plusMinutes(durationInMinutes));

        userRepository.updateEmailVerification(user);

        String subject = "Email Verification";
        String body = String.format(
                "Only one step to take full advantage of LinkedIn.\n\n" +
                        "Enter this code to verify your email: %s\n\n" +
                        "The code will expire in %s minutes.",
                emailVerificationCode, durationInMinutes
        );

        try {
            emailSender.sendEmail(email, subject, body);
        } catch (Exception e) {
            logger.info("Error while sending email: {}");
        }
    }

    //    validation
    @Override
    public void validateEmailVerificationToken(String token, String email) {
        Optional<AuthenticationUser> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with this email does not exist.");
        }

        AuthenticationUser user = userOpt.get();

        if (!passwordEncoder.matches(token, user.getEmailVerificationToken())) {
            throw new IllegalArgumentException("Invalid email verification token.");
        }

        if (user.getEmailVerificationExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Email verification token has expired.");
        }

        // If everything is valid, verify the email
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpirationDate(null);
        userRepository.updateEmailVerification(user);
    }

    //register
    @Override
    public RegisterResponse register(LoginRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<AuthenticationUser> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            throw new IllegalWriteException("Email is already used");
        }

        AuthenticationUser user = new AuthenticationUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);

        String emailVerification = generateEmailVerification();
        user.setEmailVerificationToken(passwordEncoder.encode(emailVerification));
        user.setEmailVerificationExpirationDate(LocalDateTime.now().plusMinutes(durationInMinutes));

        userRepository.saveAuthentication(user);

        String subject = "Email Verification";
        String body = String.format("""
                        Only one step to verify your account.
                        
                        Enter this code to verify your email: %s. The code will expire in %s minutes.""",
                emailVerification, durationInMinutes);

        try {
            emailSender.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            logger.info("Error while sending email: {}", e.getMessage());
        }

        return modelMapper.map(user, RegisterResponse.class);
    }

    //reset new password
    @Override
    public void resetNewPassword(String email, String newPassword, String token) throws IllegalWriteException {
        Optional<AuthenticationUser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(token, userOpt.get().getPasswordResetToken()) && !userOpt.get().getPasswordResetTokenExpirationDate().isBefore(LocalDateTime.now())) {
            userOpt.get().setPasswordResetToken(null);
            userOpt.get().setPasswordResetTokenExpirationDate(null);
            userOpt.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.updatePasswordReset(userOpt.get());
        } else if (userOpt.isPresent() && passwordEncoder.matches(token, userOpt.get().getPasswordResetToken()) && userOpt.get().getPasswordResetTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalWriteException("password reset token expired.");
        } else {
            throw new IllegalArgumentException("Password reset token failed token.");
        }
    }

    //send reset token for change password
    @Override
    public void sendPasswordResetToken(String email) {
        Optional<AuthenticationUser> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email not found.");
        }

        AuthenticationUser user = userOpt.get();

        String passwordReset = generateEmailVerification();
        String hashToken = passwordEncoder.encode(passwordReset);
        user.setPasswordResetToken(hashToken);
        user.setPasswordResetTokenExpirationDate(LocalDateTime.now().plusMinutes(durationInMinutes)); // New field
        userRepository.updatePasswordReset(user);

        String subject = "Password Reset";
        String body = String.format("""
                        Only one step to reset your password.
                        
                        Enter this code to reset your password: %s. The code will expire in %s minutes.""",
                passwordReset, durationInMinutes);

        try {
            emailSender.sendEmail(email, subject, body);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.info("Error while sending email: {}", e.getMessage());
        }
    }


    //    login
    @Override
    public LoginResponse login(LoginRequest request) {
        // Fetch user once
        AuthenticationUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is verified
        if (!user.getEmailVerified()) {
            throw new EmailNotVerifiedException("Please verify your email address first.");
        }

        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // Generate JWT Token
        String token = jwtUtils.generateToken(user.getEmail());

        // Return authentication response
        return LoginResponse.builder()
                .token(token)
                .message("Authentication Successful!")
                .build();
    }

    @Override
    public AuthenticationUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //    update profile
    public AuthenticationUser updateUserProfile(Long id,String firstName, String lastName, String company, String position, String location) {
        Long existUser = securityUtil.getCurrentUserId();
        System.out.println(existUser);
        if (existUser == null) {
            throw new IllegalArgumentException("user not found");
        }
        AuthenticationUser user = new AuthenticationUser();
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (company != null) user.setCompany(company);
        if (position != null) user.setPosition(position);
        if (location != null) user.setLocation(location);
        userRepository.updateProfileUser(user, existUser);
        return user;
    }
}


