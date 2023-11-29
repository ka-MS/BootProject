package com.example.bootproject.post.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
