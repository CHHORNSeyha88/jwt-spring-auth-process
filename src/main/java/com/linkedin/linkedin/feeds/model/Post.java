package com.linkedin.linkedin.feeds.model;

import com.linkedin.linkedin.authentication.dto.response.AuthorResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long postId;
    @NotEmpty
    private String content;
    private String picture;
    //    many to one

}
