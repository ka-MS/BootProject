package com.example.bootproject.interfaces.dto.post;

import com.example.bootproject.domain.post.SortOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "게시글 검색")
@Getter
@Setter
@Builder
public class PostSearch {

    @Schema(description = "게시글 ID")
    private Long id;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "게시글 정렬 순서", allowableValues = {"ASC","DESC"})
    private SortOrder sortOrder;
}
