package com.linkedin.linkedin.authentication.utils;

import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil  {
//    identify own post
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticationUser user) {
            return user.getId();
        }
        return null;
    }
}
