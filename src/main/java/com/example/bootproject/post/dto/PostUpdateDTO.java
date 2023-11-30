package com.example.bootproject.post.dto;

import com.example.bootproject.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostUpdateDTO {

    private PostDetailDTO postDetailDTO;
    private String message;
}
