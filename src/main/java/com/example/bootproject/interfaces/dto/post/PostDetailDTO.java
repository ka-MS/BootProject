package com.example.bootproject.interfaces.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "게시글 상세 정보")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class PostDetailDTO {

    @Schema(description = "게시글 ID")
    private Long id;
    @Schema(description = "게시글 UUID")
    private String uuid;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "게시글 생성 일자")
    private LocalDateTime createdAt;
    @Schema(description = "게시글 수정 일자")
    private LocalDateTime updatedAt;
    @Schema(description = "게시글 수정 가능 일")
    private Long modifiableDate;

    public PostUpdateDTO toPostUpdateDTO() {
        return PostUpdateDTO.builder()
                .postDetailDTO(this)
                .message(modifiableDate == 1 ? "No modifications can be made after one day" : modifiableDate + " days left")
                .build();
    }
}
