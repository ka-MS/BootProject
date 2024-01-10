package com.example.bootproject.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseEntity {

    @Schema(description = "게시글 생성 일자")
    private LocalDateTime createdAt;
    @Schema(description = "게시글 수정 일자")
    private LocalDateTime updatedAt;
    @Schema(description = "게시글 삭제 일자")
    private LocalDateTime deletedAt;

    public void interceptUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public void interceptCreatedAt(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
