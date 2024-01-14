package com.example.bootproject.mapper.post;

import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import com.example.bootproject.mapper.post.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PostMapperTest {

    @Resource
    PostMapper postMapper;

    @Transactional
    @DisplayName("getPost 게시글 get 테스트")
    @Test
    void getPost() {
        // given
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder()
                .title("Test getPost")
                .build();
        post.setUuid(uuid);

        postMapper.insertPost(post);

        // when
        Post postMapperPost = postMapper.getPostByUUID(post.getUuid());

        // then
        assertThat(postMapperPost.getTitle()).isEqualTo("Test getPost");
    }

    @Transactional
    @DisplayName("insertPost 게시글 작성 테스트")
    @Test
    void insertPost() {
        // given
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder()
                .title("Test insertPost")
                .build();
        post.setUuid(uuid);

        postMapper.insertPost(post);

        // when
        Post postMapperPost = postMapper.getPostByUUID(post.getUuid());

        // then
        assertThat(postMapperPost.getTitle()).isEqualTo("Test insertPost");
    }

    @Transactional
    @DisplayName("selectAllPosts Post 모든 목록 조회")
    @Test
    void selectAllPosts() {
        // given
        Post post = Post.builder()
                .title("Test selectAllPosts")
                .build();

        postMapper.insertPost(post);
        postMapper.insertPost(post);
        postMapper.insertPost(post);

        // when
        List<Post> posts = postMapper.selectAllPosts();

        // then
        assertThat(posts.size()).isNotZero();
    }

    @Transactional
    @DisplayName("softDeletePost softDelete 테스트")
    @Test
    void softDeletePost() {
        // given
        Post post = Post.builder()
                .title("Test softDeletePost")
                .build();

        postMapper.insertPost(post);
        String uuid = post.getUuid();

        Post postMapperPost = postMapper.getPostByUUID(uuid);

        // when
        postMapper.softDeletePost(postMapperPost.getUuid());

        System.out.println(postMapperPost.getDeletedAt());
        System.out.println(postMapperPost.getTitle());

        // then
        assertThat(postMapperPost.getDeletedAt()).isNull();
    }

    @Transactional
    @DisplayName("deletePost 게시글 삭제 테스트")
    @Test
    void deletePost() {
        // given
        Post post = Post.builder()
                .title("Test deletePost")
                .build();

        postMapper.insertPost(post);

        String uuid = post.getUuid();

        // when
        postMapper.deletePost(uuid);

        // then
        assertThat(postMapper.getPostByUUID(uuid)).isNull();
    }

    @Transactional
    @DisplayName("updatePost 게시글 업데이트 테스트")
    @Test
    void updatePost() {
        // given
        Post post = Post.builder()
                .title("Test updatePost")
                .build();
        postMapper.insertPost(post);
        String uuid = post.getUuid();
        Post changePost = Post.builder()
                .title("Title Updated")
                .build();
        changePost.setUuid(uuid);

        // when
        postMapper.updatePost(changePost);
        Post postMapperPost = postMapper.getPostByUUID(uuid);

        // then
        assertThat(postMapperPost.getTitle()).isEqualTo("Title Updated");
    }

    @Transactional
    @DisplayName("selectPostsByKeywords 게시글 검색 테스트")
    @Test
    void selectPostsByKeywords() {
        // given
        Post postOne = Post.builder()
                .title("Test selectPostsByKeywords")
                .build();
        Post postTwo = Post.builder()
                .title("Test2 selectPostsByKeywords")
                .build();
        Post postThree = Post.builder()
                .title("Test3 selectPostsByKeywords")
                .build();

        postMapper.insertPost(postOne);
        postMapper.insertPost(postTwo);
        postMapper.insertPost(postThree);

        PostSearch postSearch = PostSearch.builder()
                .title("selectPostsByKeywords")
                .build();

        // when
        List<Post> posts = postMapper.selectPostsByKeywords(postSearch);

        // then
        assertThat(posts.size()).isEqualTo(3);
    }
}