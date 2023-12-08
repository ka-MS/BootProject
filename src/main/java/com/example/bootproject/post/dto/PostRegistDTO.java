package com.example.bootproject.post.dto;

import com.example.bootproject.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRegistDTO {

    private String title;
    private String content;

    public static Post toPost(PostRegistDTO postRegistDTO) {
        return Post.builder()
                .title(postRegistDTO.getTitle())
                .content(postRegistDTO.getContent())
                .build();
    }

    public static Post toPost(Long id, PostRegistDTO postRegistDTO) {
        return Post.builder()
                .id(id)
                .title(postRegistDTO.getTitle())
                .content(postRegistDTO.getContent())
                .build();
    }
}
