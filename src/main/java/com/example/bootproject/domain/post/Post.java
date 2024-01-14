package com.example.bootproject.domain.post;


import com.example.bootproject.domain.BaseEntity;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.util.UUID;

@Schema(description = "Post 엔티티")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Post extends BaseEntity {

    @Schema(description = "게시글 ID")
    private Long id;
    @Length(min = 1, max = 200, message = "Title must be up to 200 characters.")
    @Schema(description = "게시글 제목", nullable = true, type = "String")
    private String title;
    @Pattern(regexp = "^.{0,1000}$", message = "Content must be up to 1000 characters.")
    @Schema(description = "게시글 내용", nullable = true, example = "This Post Content")
    private String content;

    public PostDetailDTO toPostDetailDTO(long modifiableDate) {
        return PostDetailDTO.builder()
                .id(id)
                .uuid(getUuid())
                .title(title)
                .content(content)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .modifiableDate(modifiableDate)
                .build();
    }
}
