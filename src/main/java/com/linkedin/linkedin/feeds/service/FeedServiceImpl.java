package com.linkedin.linkedin.feeds.service;

import com.linkedin.linkedin.authentication.dto.response.AuthorResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import com.linkedin.linkedin.authentication.repository.AuthenticationRepository;
import com.linkedin.linkedin.authentication.utils.SecurityUtil;
import com.linkedin.linkedin.feeds.dto.PostRequest;
import com.linkedin.linkedin.feeds.dto.PostResponse;
import com.linkedin.linkedin.feeds.model.Post;
import com.linkedin.linkedin.feeds.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl {
    private final FeedRepository feedRepository;
    private final SecurityUtil securityUtil;
    private final AuthenticationRepository authenticationRepository;
    private final ModelMapper modelMapper;

    public PostResponse createPost(PostRequest postRequest) {
        Long currentUserLogin = securityUtil.getCurrentUserId();
        AuthorResponse existUser = authenticationRepository.findAuthUserById(currentUserLogin);
        if(existUser == null){
            throw new IllegalArgumentException("user not found");
        }
        feedRepository.insertPost(postRequest, currentUserLogin);
        PostResponse latestPost = feedRepository.findLatestPostByAuthorId(currentUserLogin);
        return modelMapper.map(latestPost,PostResponse.class);
    }
}
