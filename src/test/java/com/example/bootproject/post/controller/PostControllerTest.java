package com.example.bootproject.post.controller;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    // object를 json으로 json을 object로
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    PostService postService;

    // api가 제대로된 response를 발송 하는지 확인
    @Test
    @DisplayName("allPosts 모든 게시글 조회 테스트")
    void allPosts() throws Exception {
        // given
        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder()
                .id(1L)
                .title("all posts test")
                .build());
        posts.add(Post.builder()
                .id(2L)
                .title("all posts test2")
                .build());

        given(postService.allPostList()).willReturn(posts);

        //when
        mockMvc.perform(get("/api/posts/all"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("all posts test"))
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    @DisplayName("get Post 특정 게시글 조회 테스트")
    void getPost() throws Exception {
        // given
        PostDetailDTO post = PostDetailDTO.builder()
                .id(1L)
                .title("all posts test")
                .build();


        // when
        given(postService.getPost(post.getId())).willReturn(post);

        // then
        mockMvc.perform(get("/api/posts/"+post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title").value("all posts test"))
                .andExpect(jsonPath("id").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("searchPost 게시글 리스트 조회 테스트")
    void searchPost() throws Exception {
        // given
        List<PostDetailDTO> posts = new ArrayList<>();
        posts.add(PostDetailDTO.builder()
                .id(1L)
                .title("all posts test")
                .createdAt(LocalDateTime.of(2023,11,29,0,0))
                .build());
        posts.add(PostDetailDTO
                .builder().id(2L)
                .title("all posts test2")
                .createdAt(LocalDateTime.of(2023,11,28,0,0))
                .build());

        // search객체의 값과 param의 값이 동일해야 정상적으로 진행됨
        given(postService.postListSearch(any(PostSearch.class))).willReturn(posts);

        // when
        mockMvc.perform(get("/api/posts")
                .param("title","test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("all posts test"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andDo(print());
    }

    @Test
    @DisplayName("insertPost 게시글 등록 테스트")
    void insertPost() throws Exception {
        // given
        PostRegistDTO rPost = PostRegistDTO.builder()
                .title("all posts test")
                .content("test")
                .build();
        PostDetailDTO post = PostDetailDTO.builder()
                .id(1L)
                .title("all posts test")
                .content("test")
                .build();
        Gson gson = new Gson();
        String content = gson.toJson(rPost);

        given(postService.insertPost(any(PostRegistDTO.class))).willReturn(post);

        // then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title").value("all posts test"))
                .andExpect(jsonPath("id").value(1))
                .andDo(print());
    }

    @Test
    void updatePost() {
    }

    @Test
    void softDeletePost() {
    }

    @Test
    void deletePost() {
    }
}