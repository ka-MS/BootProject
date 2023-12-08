package com.example.bootproject.post.service;

import com.example.bootproject.common.ApplicationYamlRead;
import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.mapper.PostMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${constant-data.modifiable-date-value}")
    private int modifiableDateValue;
    @Value("${constant-data.modification-limit-value}")
    private static int modificationLimitValue;

    public List<Post> allPostList() {
        return postMapper.selectAllPosts();
    }

    public Post getRawPost(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

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

        return Post.toPostDetailDTO(post);
    }

    public List<PostDetailDTO> postListSearch(PostSearch postSearch) {
        List<Post> postList = postMapper.selectPostsByKeywords(postSearch);
        List<PostDetailDTO> postDetailDTOList =
                postList.stream().map(Post::toPostDetailDTO).collect(Collectors.toList());

        return postDetailDTOList;
    }

    @Transactional
    public PostDetailDTO savePost(PostRegistDTO postRegistDTO) {
        Post post = PostRegistDTO.toPost(postRegistDTO);
        postMapper.insertPost(post);
        PostDetailDTO postDetailDTO = getPost(post.getId());

        return postDetailDTO;
    }

    @Transactional
    public PostUpdateDTO updatePost(PostRegistDTO postRegistDTO, Long id) {
        PostDetailDTO postDetailCheck = getPost(id);
        Long modifiableDate = postDetailCheck.getModifiableDate();
        Post post = PostRegistDTO.toPost(id, postRegistDTO);

        if (modifiableDate <= ApplicationYamlRead.getModificationLimitValue()) {
            throw new BadRequestException("Editing is overdue for post with id " + id);
        }

        postMapper.updatePost(post);

        PostDetailDTO postDetail = getPost(id);

        return PostDetailDTO.toPostUpdateDTO(postDetail);
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

    public static long getModifiableDate(LocalDateTime date) {
        LocalDateTime nowDate = LocalDateTime.now();

        long result = ApplicationYamlRead.getModifiableDateValue() - ChronoUnit.DAYS.between(date, nowDate);

        return result;
    }
}
