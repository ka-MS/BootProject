package com.example.bootproject.interfaces.controller;

import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import com.example.bootproject.interfaces.dto.post.PostRegistDTO;
import com.example.bootproject.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

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

    // Object to json and json to Object
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    PostService postService;

    // Check whether the API sends the correct response
    @Test
    @DisplayName("insertPost validation 실패 테스트 Title 200자 이상")
    void insertPost_WhenTileIsMoreThan200() throws Exception {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title(String.join("", Collections.nCopies(201, "T")))
                .content("test")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(postRegistDTO);

        // when
        // then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.message").value("Title must be up to 200 characters."));
        ;
    }

    @Test
    @DisplayName("insertPost validation 실패 테스트 Content 1000자 이상")
    void insertPost_WhenContentIsMoreThan1000() throws Exception {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title("Content Fail Test")
                .content(String.join("", Collections.nCopies(1001, "T")))
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(postRegistDTO);

        // when
        // then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.message").value("Content must be up to 1000 characters."));
        ;
    }

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

        // when
        mockMvc.perform(get("/api/posts/all"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("all posts test"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("get Post 특정 게시글 조회 테스트")
    void getPost() throws Exception {
        // given
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        PostDetailDTO post = PostDetailDTO.builder()
                .id(1L)
                .title("all posts test")
                .build();
        post.setUuid(uuid);

        // when
        given(postService.getPost(uuid)).willReturn(post);

        // then
        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title").value("all posts test"))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("uuid").value("4e610b8f-af80-11ee-a127-00155dddd5ce"))
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
                .createdAt(LocalDateTime.of(2023, 11, 29, 0, 0))
                .build());
        posts.add(PostDetailDTO
                .builder().id(2L)
                .title("all posts test2")
                .createdAt(LocalDateTime.of(2023, 11, 28, 0, 0))
                .build());

        // The search object value and the param value must be the same to proceed normally.
        given(postService.postListSearch(any(PostSearch.class))).willReturn(posts);

        // when then
        mockMvc.perform(get("/api/posts")
                        .param("title", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("all posts test"))
                .andExpect(jsonPath("$", hasSize(2)))
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

        // when
        given(postService.savePost(any(PostRegistDTO.class))).willReturn(post);

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
}