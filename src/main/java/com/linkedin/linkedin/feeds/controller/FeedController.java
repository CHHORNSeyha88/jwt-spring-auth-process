package com.linkedin.linkedin.feeds.controller;

import com.linkedin.linkedin.feeds.dto.PostRequest;
import com.linkedin.linkedin.feeds.dto.PostResponse;
import com.linkedin.linkedin.httpreponse.ResponseAPI;
import com.linkedin.linkedin.feeds.service.FeedServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/feed")
@SecurityRequirement(name = "bearerAuth")

public class FeedController {
    private final FeedServiceImpl feedService;

    @PostMapping
    public ResponseEntity<ResponseAPI<PostResponse>> createPost(@RequestBody PostRequest postRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseAPI.<PostResponse>builder()
                        .message("create post successfully!")
                        .payload(feedService.createPost(postRequest))
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }
}
