package com.example.bootproject.mapper.post.mapper;

import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PostMapper {

    List<Post> selectAllPosts();
    void insertPost(Post post);

    void softDeletePost(String id);

    void deletePost(String id);

    Post getPostByUUID(String uuid);

    Post getPostById(Long id);

    void updatePost(Post post);

    List<Post> selectPostsByKeywords(PostSearch postSearch);
}
