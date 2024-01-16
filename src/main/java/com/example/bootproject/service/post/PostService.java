package com.example.bootproject.service.post;

import com.example.bootproject.domain.BaseEntity;
import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import com.example.bootproject.interfaces.dto.post.PostRegistDTO;
import com.example.bootproject.interfaces.dto.post.PostUpdateDTO;
import com.example.bootproject.mapper.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final LocalDateTime nowDate = LocalDateTime.now();

    @Value("${constant-data.modifiable-date-value}")
    private int modifiableDateValue;

    @Value("${constant-data.modification-limit-value}")
    private int modificationLimitValue;

    @Transactional(readOnly = true)
    public List<Post> allPostList() {
        return postMapper.selectAllPosts();
    }

    @Transactional(readOnly = true)
    public Post getRawPost(Long id) {
        Post post = postMapper.getPostById(id);

        if (post == null) {
            throw new NotFoundException("There is no post with id: " + id);
        }

        return post;
    }

    @Transactional(readOnly = true)
    public PostDetailDTO getPost(String uuid) {
        Post post = postMapper.getPostByUUID(uuid);

        validatePostExistence(post, uuid);

        if (post.getDeletedAt() != null) {
            throw new BadRequestException("Post with id " + uuid + " is already deleted");
        }

        Long modifiableDate = getModifiableDate(post.getCreatedAt());

        return post.toPostDetailDTO(modifiableDate);
    }

    @Transactional(readOnly = true)
    public List<PostDetailDTO> postListSearch(PostSearch postSearch) {
        List<Post> postList = postMapper.selectPostsByKeywords(postSearch);

        List<PostDetailDTO> postDetails =
                postList.stream()
                        .map(post -> post.toPostDetailDTO(getModifiableDate(post.getCreatedAt())))
                        .collect(Collectors.toList());

        return postDetails;
    }

    @Transactional
    public PostDetailDTO savePost(PostRegistDTO postRegistDTO) {
        Post post = postRegistDTO.toPost();
        postMapper.insertPost(post);
        PostDetailDTO postDetailDTO = getPost(post.getUuid());

        return postDetailDTO;
    }

    @Transactional
    public PostUpdateDTO updatePost(PostRegistDTO postRegistDTO, String uuid) {
        PostDetailDTO postDetailCheck = getPost(uuid);
        Long modifiableDate = postDetailCheck.getModifiableDate();
        Post post = postRegistDTO.toPost(uuid);

        if (modifiableDate <= modificationLimitValue) {
            throw new BadRequestException("Editing is overdue for post with id " + uuid);
        }

        postMapper.updatePost(post);

        PostDetailDTO postDetail = getPost(uuid);

        return postDetail.toPostUpdateDTO();
    }

    @Transactional
    public void deletePost(String uuid) {
        Post post = postMapper.getPostByUUID(uuid);

        validatePostExistence(post, uuid);

        postMapper.deletePost(uuid);
    }

    @Transactional
    public void softDeletePost(String uuid) {
        Post post = postMapper.getPostByUUID(uuid);

        validatePostExistence(post, uuid);

        if (post.getDeletedAt() == null) {
            postMapper.softDeletePost(uuid);
        } else {
            throw new BadRequestException("The post with id " + uuid + " has already been deleted");
        }
    }

    public void validatePostExistence(Post post, String uuid) {
        if (post == null) {
            throw new NotFoundException("There is no post with id: " + uuid);
        }
    }

    public Long getModifiableDate(LocalDateTime date) {
        try {
            return modifiableDateValue - ChronoUnit.DAYS.between(date, nowDate);
        } catch (NullPointerException e) {
            log.warn("Creation Time is null", e);
            return null;
        }
    }
}
