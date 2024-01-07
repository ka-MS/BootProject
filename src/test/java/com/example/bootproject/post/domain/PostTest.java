package com.example.bootproject.post.domain;

import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class PostTest {

    @Autowired
    PostService postService;

    @DisplayName("toPostDetailDTOTest builder method Test")
    @Test
    void toPostDetailDTOTest() {
        // given
        Post post = Post.builder()
                .id(1L)
                .title("Test title")
                .content("Test content")
                .build();
        post.setCreatedAt(LocalDateTime.now());

        // when
        PostDetailDTO postDetailDTO = post.toPostDetailDTO(postService.getModifiableDate(post.getCreatedAt()));

        // then
        assertThat(postDetailDTO.getId()).isEqualTo(1L);
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test title");
        assertThat(postDetailDTO.getContent()).isEqualTo("Test content");
    }
}