package com.example.bootproject.post.controller;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.service.PostService;
import lombok.AllArgsConstructor;
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
        List<Post> posts = postService.getPostList();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPost(){
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("")
    public ResponseEntity searchPost(){
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("")
    public ResponseEntity insertPost(Post post){

        return ResponseEntity.ok().body(postService.insertPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(){
        return ResponseEntity.ok().body("success");
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity softDeletePost(){
        return ResponseEntity.ok().body("success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(){
        return ResponseEntity.ok().body("success");
    }

}
