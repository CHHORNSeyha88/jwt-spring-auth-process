package com.linkedin.linkedin.authentication.controller;

import com.linkedin.linkedin.authentication.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/post")
@SecurityRequirement(name = "bearerAuth")
public class PostController {
    private final SecurityUtil securityUtil;

    public PostController(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public String getAllPost(){
        return "Get all Post";
    }
    @GetMapping("/me")
    public String testCurrentUser() {
        Long user = securityUtil.getCurrentUserId();
        System.out.println("Logged-in user: " + user);
        return "Hello, " + user; // or any field you want
    }
}
