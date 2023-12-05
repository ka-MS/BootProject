package com.example.bootproject.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private Long id;
    private String title;
    private String content;
    private SortOrder sortOrder;
}
enum SortOrder{ ASC, DESC;}