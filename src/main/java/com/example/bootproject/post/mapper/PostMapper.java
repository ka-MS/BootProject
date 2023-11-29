package com.example.bootproject.post.mapper;

import com.example.bootproject.post.domain.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PostMapper {

    List<Post> selectAllPosts();
    int insertPost(Post post);
}
