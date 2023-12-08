package com.example.bootproject.post.domain;


import com.example.bootproject.common.ApplicationYamlRead;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.service.PostService;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity {

    private Long id;
    private String title;
    private String content;

    public static class PostBuilder {

        public PostBuilder title(String title) {
            if (!title.matches("^.{3,200}$")) {
                throw new IllegalArgumentException("Content must be up to 200 characters.");
            }
            this.title = title;
            return this;
        }

        public PostBuilder content(String content) {
            if (content != null && !content.matches("^.{0,1000}$")) {
                throw new IllegalArgumentException("Content must be up to 1000 characters.");
            }
            this.content = content;
            return this;
        }
    }

    public static PostDetailDTO toPostDetailDTO(Post post) {
        return PostDetailDTO.builder().id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .modifiableDate(PostService.getModifiableDate(post.getCreatedAt()))
                .build();
    }
}
