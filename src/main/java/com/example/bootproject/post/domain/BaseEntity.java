package com.example.bootproject.post.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void interceptUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public void interceptCreatedAt(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
