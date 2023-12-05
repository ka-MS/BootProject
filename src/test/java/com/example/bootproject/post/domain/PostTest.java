package com.example.bootproject.post.domain;

import com.example.bootproject.common.ApplicationYamlRead;
import com.example.bootproject.post.dto.PostDetailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class PostTest {

    @Test
    void toPostDetailDTOTest() {
        Post post = Post.builder()
                .id(1L)
                .title("Test title")
                .content("Test content")
                .build();

        PostDetailDTO postDetailDTO = Post.toPostDetailDTO(post);

        assertThat(postDetailDTO.getId()).isEqualTo(1L);
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test title");
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test content");
    }

    @Test
    void getModifiableDateTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(2023,11,29,0,0);
        long compareDateValue = ApplicationYamlRead.getModifiableDateValue() - ChronoUnit.DAYS.between(date, now);

        long modifiableDate = Post.getModifiableDate(date);

        System.out.println("modifiableDate : " + modifiableDate);
        assertThat(modifiableDate).isEqualTo(compareDateValue);
    }
}