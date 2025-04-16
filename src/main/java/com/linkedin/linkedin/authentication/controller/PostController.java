package com.linkedin.linkedin.authentication.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/post")
@SecurityRequirement(name = "bearerAuth")
public class PostController {
    @GetMapping
    public String getAllPost(){
        return "Get all Post";
    }
}
