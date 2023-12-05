package com.example.bootproject.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class ApplicationYamlReadTest {

    @Test
    void modifiableDateTest(){
        int modifiableDateValue = ApplicationYamlRead.modifiableDateValue;

        System.out.println("modifiableDate : " + modifiableDateValue);
        assertThat(modifiableDateValue).isEqualTo(10);
    }

    @Test
    void modificationLimitTest(){
        int modificationLimitValue = ApplicationYamlRead.modificationLimitValue;

        System.out.println("modificationLimit : " + modificationLimitValue);
        assertThat(modificationLimitValue).isZero();
    }

}