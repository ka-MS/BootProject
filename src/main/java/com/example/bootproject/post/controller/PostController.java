package com.example.bootproject.post.controller;

import com.example.bootproject.post.enums.DeleteType;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "posts", description = "Post API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "View all posts", description = "View all registered posts.", tags = {"posts"})
    @GetMapping("/api/posts/all")
    public List<Post> allPosts() {
        return postService.allPostList();
    }

    @Operation(summary = "Post View", description = "Search for posts with a specific ID.", tags = {"posts"})
    @ApiResponse(responseCode = "200", description = "Post view successful")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @GetMapping("/api/posts/{postId}")
    public PostDetailDTO getPost(@PathVariable("postId") Long id) {
        return postService.getPost(id);
    }

    @Operation(summary = "Post Search", description = "Search for posts that meet the conditions.", tags = {"posts"})
    @GetMapping("/api/posts")
    public List<PostDetailDTO> searchPost(PostSearch search) {
        return postService.postListSearch(search);
    }

    @Operation(summary = "Create post", description = "Register a new post.", tags = {"posts"})
    @ApiResponse(responseCode = "201", description = "Post creation successful")
    @PostMapping("/api/posts")
    public ResponseEntity<PostDetailDTO> insertPost(@RequestBody PostRegistDTO post) {
        PostDetailDTO postDetail = postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDetail);
    }

    @Operation(summary = "Update post", description = "Edit the content of a specific post.", tags = {"posts"})
    @PutMapping("/api/posts/{postId}")
    public PostUpdateDTO updatePost(@PathVariable("postId") Long id, @RequestBody PostRegistDTO postRegistDTO) {
        return postService.updatePost(postRegistDTO, id);
    }

    @Operation(summary = "Delete post", description = "Delete a specific post.", tags = {"posts"})
    @DeleteMapping("/api/posts/{postId}")
    public void deletePost(@PathVariable("postId") Long id, @RequestParam("deleteType") DeleteType deleteType) {
        if (deleteType.equals(DeleteType.HARD)){
            postService.deletePost(id);
        } else {
            postService.softDeletePost(id);
        }
    }
}