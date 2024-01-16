package com.example.bootproject.interfaces.dto.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostDetailDTOTest {

    @DisplayName("toPostUpdateDTO Builder Test 수정 기한이 이틀 이상 남았을 때 ")
    @Test
    void toPostUpdateDTO() {
        // given
        PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                .id(1L)
                .modifiableDate(2L)
                .build();

        // when
        PostUpdateDTO postUpdateDTO = postDetailDTO.toPostUpdateDTO();

        // then
        assertThat(postUpdateDTO.getPostDetailDTO()).isEqualTo(postDetailDTO);
        assertThat(postUpdateDTO.getMessage()).isEqualTo(postDetailDTO.getModifiableDate() + " days left");
    }

    @DisplayName("toPostUpdateDTO Builder Test 수정 기한이 하루 남았을 때 ")
    @Test
    void toPostUpdateDTO_WhenLeftOneDay() {
        // given
        PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                .id(1L)
                .modifiableDate(1L)
                .build();

        // when
        PostUpdateDTO postUpdateDTO = postDetailDTO.toPostUpdateDTO();

        // then
        assertThat(postUpdateDTO.getPostDetailDTO()).isEqualTo(postDetailDTO);
        assertThat(postUpdateDTO.getMessage()).isEqualTo("No modifications can be made after one day");
    }
}