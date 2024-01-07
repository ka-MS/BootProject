package com.example.bootproject.post.service;

import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final LocalDateTime nowDate = LocalDateTime.now();

    @Value("${constant-data.modifiable-date-value}")
    private int modifiableDateValue;

    @Value("${constant-data.modification-limit-value}")
    private int modificationLimitValue;

    public List<Post> allPostList() {
        return postMapper.selectAllPosts();
    }

    public Post getRawPost(Long id) {
        Post post = postMapper.getPost(id);

        if (post == null) {
            throw new NotFoundException("There is no post with id: " + id);
        }

        return post;
    }

    public PostDetailDTO getPost(Long id) {
        Post post = getRawPost(id);

        if (post.getDeletedAt() != null) {
            throw new BadRequestException("Post with id " + id + " is already deleted");
        }

        long modifiableDate = getModifiableDate(post.getCreatedAt());

        return post.toPostDetailDTO(modifiableDate);
    }

    public List<PostDetailDTO> postListSearch(PostSearch postSearch) {
        List<Post> postList = postMapper.selectPostsByKeywords(postSearch);

        List<PostDetailDTO> postDetails=
                postList.stream()
                        .map(post -> post.toPostDetailDTO(getModifiableDate(post.getCreatedAt())))
                        .collect(Collectors.toList());

        return postDetails;
    }

    @Transactional
    public PostDetailDTO savePost(PostRegistDTO postRegistDTO) {
        Post post = postRegistDTO.toPost();
        postMapper.insertPost(post);
        PostDetailDTO postDetailDTO = getPost(post.getId());

        return postDetailDTO;
    }

    @Transactional
    public PostUpdateDTO updatePost(PostRegistDTO postRegistDTO, Long id) {
        PostDetailDTO postDetailCheck = getPost(id);
        Long modifiableDate = postDetailCheck.getModifiableDate();
        Post post = postRegistDTO.toPost(id);

        if (modifiableDate <= modificationLimitValue) {
            throw new BadRequestException("Editing is overdue for post with id " + id);
        }

        postMapper.updatePost(post);

        PostDetailDTO postDetail = getPost(id);

        return postDetail.toPostUpdateDTO();
    }

    @Transactional
    public void deletePost(Long id) {
        getRawPost(id); // 게시글 유무 체크

        postMapper.deletePost(id);
    }

    @Transactional
    public void softDeletePost(Long id) {
        Post post = getRawPost(id);

        if (post.getDeletedAt() == null) {
            postMapper.softDeletePost(id);
        } else {
            throw new BadRequestException("The post with id " + id + " has already been deleted");
        }
    }

    public long getModifiableDate(LocalDateTime date) {
        return modifiableDateValue - ChronoUnit.DAYS.between(date, nowDate);
    }
}
