package com.example.bootproject.post.service;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class PostService {

    final PostMapper postMapper;

    public List<Post> allPostList() {
        return postMapper.selectAllPosts();
    }

    public Post getRawPost(Long id) {
        Post post = postMapper.getPost(id);
        if(post == null) {
            throw new IllegalArgumentException("게시글이 없어");
        }
        return post;
    }
    public PostDetailDTO getPost(Long id){
        Post post = getRawPost(id);
        if(post.getDeletedAt() != null){
            throw new IllegalStateException("삭제된 게시물");
        }
        return PostDetailDTO.builder().id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .modifiableDate(getModifiableDate(post.getCreatedAt()))
                .build();
    }

    @Transactional
    public PostDetailDTO insertPost(PostRegistDTO rPost){
        // 정규식체크추가
        Post post = Post.builder()
                .title(rPost.getTitle())
                .content(rPost.getContent())
                .build();
        postMapper.insertPost(post);
        return getPost(post.getId());
    }
    @Transactional
    public PostUpdateDTO updatePost(PostRegistDTO rPost, Long id){
        PostDetailDTO dPost = getPost(id);
        Long modifiableDate = dPost.getModifiableDate();
        if(modifiableDate <= 0){
            throw new IllegalStateException("수정 기한이 끝난 게시물");
        }
        postMapper.updatePost(
                Post.builder()
                        .id(id)
                        .title(rPost.getTitle())
                        .content(rPost.getContent())
                        .build());
        return PostUpdateDTO.builder()
                .postDetailDTO(getPost(id))
                .message(modifiableDate == 1 ? "하루가 지나면 수정을 못한다" : modifiableDate+"일 남음")
                .build();
    }

    @Transactional
    public void softDeletePost(Long id) {
        Post post = getRawPost(id);
        if(post.getDeletedAt() != null){
            throw new IllegalStateException("삭제된 게시물");
        }
        int result = postMapper.softDeletePost(id);
        if (result == 0)
            throw new IllegalStateException("없는 게시물");
    }

    @Transactional
    public void deletePost(Long id){
        int result = postMapper.deletePost(id);
        if (result == 0)
            throw new IllegalStateException("없는 게시물");
    }

    public long getModifiableDate(LocalDateTime date) {
        LocalDateTime nowDate = LocalDateTime.now();
        return 10-ChronoUnit.DAYS.between(date, nowDate);
    }


}
