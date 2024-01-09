package com.example.bootproject.interfaces.dto.post;

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

    public PostUpdateDTO toPostUpdateDTO() {
        return PostUpdateDTO.builder()
                .postDetailDTO(this)
                .message(modifiableDate == 1 ? "No modifications can be made after one day" : modifiableDate + " days left")
                .build();
    }
}
