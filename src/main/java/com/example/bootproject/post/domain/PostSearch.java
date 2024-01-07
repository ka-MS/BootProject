package com.example.bootproject.post.domain;

import com.example.bootproject.post.enums.SortOrder;
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
