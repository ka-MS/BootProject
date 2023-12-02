package com.example.bootproject.post.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch {

    private Long id;
    private String title;
    private String content;
    private SortOrder sortOrder;

}
enum SortOrder{ ASC, DESC;}