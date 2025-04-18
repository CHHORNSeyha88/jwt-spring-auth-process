package com.linkedin.linkedin.feeds.repository;

import com.linkedin.linkedin.feeds.dto.PostRequest;
import com.linkedin.linkedin.feeds.dto.PostResponse;
import com.linkedin.linkedin.feeds.model.Post;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FeedRepository {
    @Insert("""
    INSERT INTO posts(content, picture, author_id)
    VALUES (#{postRequest.content}, #{postRequest.picture}, #{authorId})
""")
    void insertPost(@Param("postRequest") PostRequest postRequest, @Param("authorId") Long authorId);
    @Select("SELECT * FROM posts WHERE id = #{id}")
    Post findPostById(Long id);
    @Select("""
    SELECT * FROM posts\s
    WHERE author_id = #{authorId}
    ORDER BY id DESC
    LIMIT 1
""")
    @Result(property = "postId", column = "id")
    @Result(property = "authorResponse",column = "author_id", one = @One(select = "com.linkedin.linkedin.authentication.repository.AuthenticationRepository.findAuthUserById"))
    PostResponse findLatestPostByAuthorId(@Param("authorId") Long authorId);

}
