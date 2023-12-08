package com.example.bootproject.post.controller;

import com.example.bootproject.enums.DeleteType;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//초기화 되지 않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 준다(생성자 주입)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts/all")
    public List<Post> allPosts() {
        List<Post> posts = postService.allPostList();

        return posts;
    }

    @GetMapping("/api/posts/{postId}")
    public PostDetailDTO getPost(@PathVariable("postId") Long id) {
        PostDetailDTO postDetailDTO = postService.getPost(id);

        return postDetailDTO;
    }

    @GetMapping("/api/posts")
    public List<PostDetailDTO> searchPost(PostSearch search) {
        List<PostDetailDTO> postDetails = postService.postListSearch(search);

        return postDetails;
    }

    @PostMapping("/api/posts")
    public ResponseEntity<PostDetailDTO> insertPost(@RequestBody PostRegistDTO post) {
        PostDetailDTO postDetail = postService.savePost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(postDetail);
    }

    @PutMapping("/api/posts/{postId}")
    public PostUpdateDTO updatePost(@PathVariable("postId") Long id, @RequestBody PostRegistDTO postRegistDTO) {
        PostUpdateDTO postUpdate = postService.updatePost(postRegistDTO, id);

        return postUpdate;
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