package com.example.bootproject.post.domain;


import com.example.bootproject.post.dto.PostDetailDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private Long id;
    private String title;
    private String content;

    @Builder
    public Post(Long id, String title, String content) {
        if (title != null && !title.matches("^.{3,200}$")) {
            throw new IllegalArgumentException("Title must be up to 200 characters.");
        }
        if (content != null && !content.matches("^.{0,1000}$")) {
            throw new IllegalArgumentException("Content must be up to 1000 characters.");
        }
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public PostDetailDTO toPostDetailDTO(long modifiableDate) {
        return PostDetailDTO.builder().id(id)
                .title(title)
                .content(content)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .modifiableDate(modifiableDate)
                .build();
    }
}
