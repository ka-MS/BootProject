package com.example.bootproject.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class BaseEntity {

    @Schema(description = "UUID")
    private String uuid;
    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;
    @Schema(description = "수정 일자")
    private LocalDateTime updatedAt;
    @Schema(description = "삭제 일자")
    private LocalDateTime deletedAt;

    public void interceptUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public void interceptCreatedAt(){
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
