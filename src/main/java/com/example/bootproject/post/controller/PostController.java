package com.example.bootproject.post.controller;

import com.example.bootproject.post.enums.DeleteType;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
// Creates a constructor for uninitialized final fields or fields marked with @NonNull (constructor injection).
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts/all")
    public List<Post> allPosts() {
        return postService.allPostList();
    }

    @GetMapping("/api/posts/{postId}")
    public PostDetailDTO getPost(@PathVariable("postId")@NotNull Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/api/posts")
    public List<PostDetailDTO> searchPost(PostSearch search) {
        return postService.postListSearch(search);
    }

    @PostMapping("/api/posts")
    public ResponseEntity<PostDetailDTO> insertPost(@RequestBody PostRegistDTO post) {
        PostDetailDTO postDetail = postService.savePost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(postDetail);
    }

    @PutMapping("/api/posts/{postId}")
    public PostUpdateDTO updatePost(@PathVariable("postId") Long id, @RequestBody PostRegistDTO postRegistDTO) {
        return postService.updatePost(postRegistDTO, id);
    }

    @DeleteMapping("/api/posts/{postId}")
    public void deletePost(@PathVariable("postId") Long id, @RequestParam("deleteType") DeleteType deleteType) {
        if (deleteType.equals(DeleteType.HARD)){
            postService.deletePost(id);
        } else {
            postService.softDeletePost(id);
        }
    }
}