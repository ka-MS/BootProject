package com.example.bootproject.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PostDetailDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long modifiableDate;

    public static PostUpdateDTO toPostUpdateDTO(PostDetailDTO postDetailDTO) {
        Long modifiableDate = postDetailDTO.getModifiableDate();

        return PostUpdateDTO.builder()
                .postDetailDTO(postDetailDTO)
                .message(modifiableDate == 1 ? "No modifications can be made after one day" : modifiableDate + " days left")
                .build();
    }
}
