package com.example.bootproject.interfaces.dto.post;

import com.example.bootproject.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRegistDTO {

    private String title;
    private String content;

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Post toPost(Long postId) {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(title)
                .build();
    }
}
