package com.example.bootproject.interfaces.dto.post;

import com.example.bootproject.domain.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Schema(description = "게시글 수정 정보")
@Getter
@Builder
public class PostRegistDTO {

    @Length(min = 1, max = 200)
    @Schema(description = "게시글 제목")
    private String title;
    @Pattern(regexp = "^.{0,1000}$")
    @Schema(description = "게시글 내용")
    private String content;

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Post toPost(Long postId) {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(title)
                .build();
    }
}
