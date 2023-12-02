package com.example.bootproject.post.mapper;

import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.domain.PostSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PostMapper {

    List<Post> selectAllPosts();
    Long insertPost(Post post);

    int softDeletePost(Long id);

    int deletePost(Long id);

    Post getPost(Long id);

    Long updatePost(Post post);

    List<Post> selectPostsByKeywords(PostSearch postSearch);
}
