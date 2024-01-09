package com.example.bootproject.interfaces.dto.post;

import com.example.bootproject.domain.post.SortOrder;
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
