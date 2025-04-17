package com.linkedin.linkedin.authentication.utils;

import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import com.linkedin.linkedin.authentication.repository.AuthenticationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil  {
    private final AuthenticationRepository userRepository;

    public SecurityUtil(AuthenticationRepository userRepository) {
        this.userRepository = userRepository;
    }
    //    identify own post
    public Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthenticationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }


}

