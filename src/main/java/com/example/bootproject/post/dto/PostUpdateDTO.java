package com.example.bootproject.post.dto;

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
