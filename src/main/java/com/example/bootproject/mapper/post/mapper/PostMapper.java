package com.example.bootproject.mapper.post.mapper;

import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PostMapper {

    List<Post> selectAllPosts();
    void insertPost(Post post);

    void softDeletePost(Long id);

    void deletePost(Long id);

    Post getPost(Long id);

    void updatePost(Post post);

    List<Post> selectPostsByKeywords(PostSearch postSearch);
}
