package com.linkedin.linkedin.feeds.dto;

import com.linkedin.linkedin.authentication.dto.response.AuthorResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private String content;
    private String picture;
    private AuthorResponse authorResponse;
}
