package com.example.bootproject.interfaces.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 수정 응답")
@Getter
@Setter
@Builder
public class PostUpdateDTO {

    @Schema(description = "게시글 상세 정보")
    private PostDetailDTO postDetailDTO;
    @Schema(description = "게시글 수정 가능 여부 메세지")
    private String message;
}
