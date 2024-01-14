package com.example.bootproject;

import com.example.bootproject.mapper.post.mapper.PostMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("classpath:application-test.yml")
public class TestConfig {

    @Bean
    public PostMapper postMapper(){
        return mock(PostMapper.class);
    }

}
