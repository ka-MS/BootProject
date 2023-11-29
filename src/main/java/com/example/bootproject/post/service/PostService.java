package com.example.bootproject.post.service;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PostService {

    final PostMapper postMapper;

    public void createPost(Post post) {
        post.checkContent(post.getContent());
        Post.builder().title(post.getTitle()).build();
    }

    public List<Post> getPostList() {
        return postMapper.selectAllPosts();
    }

    public int insertPost(Post post){
        post.checkTitle(post.getTitle());
        int id = postMapper.insertPost(post);
        return id;
    }
}
