package com.example.bootproject.post.controller;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<Post>> allPosts(){
        List<Post> posts = postService.allPostList();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDTO> getPost(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(postService.getPost(id));
    }

    @GetMapping("")
    public ResponseEntity searchPost(){
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("")
    public ResponseEntity<PostDetailDTO> insertPost(PostRegistDTO post){
        return ResponseEntity.ok().body(postService.insertPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostUpdateDTO> updatePost(@PathVariable("id") Long id,PostRegistDTO post){
        return ResponseEntity.ok().body(postService.updatePost(post, id));
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity softDeletePost(@PathVariable("id") Long id){
        postService.softDeletePost(id);
        return ResponseEntity.ok().body("success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.ok().body("success");
    }

}
