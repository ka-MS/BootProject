package com.example.bootproject.interfaces.dto.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class PostRegistDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("validation 실패 테스트 Title 200자 이상")
    void validTest_WhenTileIsMoreThan200() {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title(String.join("", Collections.nCopies(201, "T"))).build();

        // when
        Set<ConstraintViolation<PostRegistDTO>> violations = validator.validate(postRegistDTO);
        Iterator<ConstraintViolation<PostRegistDTO>> iterator = violations.iterator();
        List<String> messages = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<PostRegistDTO> next = iterator.next();
            messages.add(next.getMessage());
        }

        // then
        assertThat(messages).contains("Title must be up to 200 characters.");
    }

    @Test
    @DisplayName("validation 실패 테스트 Content 1000자 이상")
    void validTest_WhenContentIsMoreThan1000() {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title("Content Fail Test").content(String.join("", Collections.nCopies(1001, "T"))).build();

        // when
        Set<ConstraintViolation<PostRegistDTO>> violations = validator.validate(postRegistDTO);
        Iterator<ConstraintViolation<PostRegistDTO>> iterator = violations.iterator();
        List<String> messages = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<PostRegistDTO> next = iterator.next();
            messages.add(next.getMessage());
        }

        // then
        assertThat(messages).contains("Content must be up to 1000 characters.");
    }
}