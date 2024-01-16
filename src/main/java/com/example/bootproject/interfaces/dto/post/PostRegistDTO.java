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

    @Length(min = 1, max = 200, message = "Title must be up to 200 characters.")
    @Schema(description = "게시글 제목")
    private String title;
    @Pattern(regexp = "^.{0,1000}$", message = "Content must be up to 1000 characters.")
    @Schema(description = "게시글 내용")
    private String content;

    // Save Post
    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    // Update Post with Uuid
    public Post toPost(String uuid) {
        return Post.builder()
                .uuid(uuid)
                .title(title)
                .content(content)
                .build();
    }
}
