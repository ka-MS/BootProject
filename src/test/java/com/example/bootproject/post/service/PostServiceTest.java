package com.example.bootproject.post.service;

import com.example.bootproject.common.ApplicationYamlRead;
import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.post.domain.Post;
import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.dto.PostRegistDTO;
import com.example.bootproject.post.dto.PostUpdateDTO;
import com.example.bootproject.post.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class PostServiceTest {

    //db 상호작용 없이 테스트 진행
    @Mock
    private PostMapper postMapper;

    private PostService postService;

    // 매 테스트 마다 진행
    @BeforeEach
    void setUp() {
        //테스트 클래스에 선언된 모든 Mock객체 초기화
        MockitoAnnotations.initMocks(this);
        // bootTest 사용하지 않으므로 수동 객체 생성
        postService = new PostService(postMapper);
    }

    @DisplayName("getRawPost 성공 테스트")
    @Test
    void getRawPost() {
        //given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getRawPost").build();

        given(postMapper.getPost(postId)).willReturn(post);

        //when
        Post rawPost = postService.getRawPost(postId);

        //then
        assertThat(rawPost.getId()).isEqualTo(1L);
        assertThat(rawPost.getTitle()).isEqualTo("Test getRawPost");
    }

    @DisplayName("getRawPost 실패 테스트 데이터 없는 경우")
    @Test
    void getRawPost_WhenPostDoseNotExist() throws Exception {
        //given
        Long postId = 1L;

        given(postMapper.getPost(postId)).willReturn(null);

        //when
        //then
        assertThatThrownBy(() -> postService.getRawPost(postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("There is no post with id: " + postId);
    }

    @DisplayName("getRawPost 실패 테스트 ID 값이 null인 경우")
    @Test
    void getRawPost_WhenPostIdIsNull() throws Exception {
        //given
        Long postId = null;

        //when
        //then
        assertThatThrownBy(() -> postService.getRawPost(postId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID cannot be null");
    }

    @DisplayName("getPost 성공 테스트")
    @Test
    void getPost() {
        //given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getPost").build();
        post.setCreatedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        //when
        PostDetailDTO postDetail = postService.getPost(postId);

        //then
        assertThat(postDetail.getId()).isEqualTo(1L);
        assertThat(postDetail.getTitle()).isEqualTo("Test getPost");
    }

    @DisplayName("getPost 실패 테스트 softDelete 된 경우")
    @Test
    void getPost_WhenPostAlreadySoftDeleted() {
        //given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getPost_WhenPostAlreadySoftDeleted").build();
        post.setDeletedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        //when
        //then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Post with id " + postId + " is already deleted");
    }

    @DisplayName("getPost 실패 테스트 데이터 없는 경우")
    @Test
    void getPost_WhenPostDoseNotExist() {
        //given
        Long postId = 1L;
        given(postMapper.getPost(postId)).willReturn(null);

        //when
        //then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("There is no post with id: " + postId);
    }

    // db 연결 없이 테스트 하고 싶은데 어떻게 해야할 지 모르겠슴
//    @Transactional
//    @DisplayName("savePost 성공 테스트")
//    @Test
//    void savePost() {
//        //given
//        PostRegistDTO postRegist = PostRegistDTO.builder().title("Test savePost").build();
//
//        //when
//        PostDetailDTO postDetailDTO = postService.savePost(postRegist);
//        // postMapper를 모킹중이므로 insert를 하더라도 id값이 업데이트 되지 않음
//
//        //then
//        assertThat(postDetailDTO.getTitle()).isEqualTo("Test savePost");
//    }

    @DisplayName("savePost 성공 테스트")
    @Test
    void savePost() {
        //given
        PostRegistDTO postRegist = PostRegistDTO.builder().title("Test savePost").build();
        Post post = Post.builder().id(1L).title("Test savePost").build();
        post.setCreatedAt(LocalDateTime.now());

        /*
        * doAnswer : void 메서드에 대한 동작을 정의할 때 사용. 
        Answer 인터페이스의 구현을 인자로 받으며 호출시 실행될 코드를 포함
        * invocation : 람다식을 이용하여 Answer 인터페이스의 구현을 제공 실행될 코드 기입. 
        invocation은 메서드 호출정보를 포함하고 있는 InvocationMock 객체
        * invocation.getArgument(0) : 모깅된 메서드에 전달 될 첫번째 인자(Post객체)를 가져옴
        * ReflectionTestUtils : 테스트 환경에서 private 필드를 조작할 때 사용
        * ReflectionTestUtils.setField(returnPost,"id",1L) : post객체에 id값을 1로 설정
        * when(postMapper).insertPost(any(Post.class)) : 어떤 Post객체가 들어와도 정의된 동작을 실행하겠다는 의미
         */
        doAnswer(invocation -> {
            Post returnPost = invocation.getArgument(0);
            ReflectionTestUtils.setField(returnPost, "id", 1L);
            return null;
        }).when(postMapper).insertPost(any(Post.class));

        given(postMapper.getPost(anyLong())).willReturn(post);

        //when
        PostDetailDTO postDetailDTO = postService.savePost(postRegist);

        //then
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test savePost");
    }

    @DisplayName("savePost 실패 테스트 Title 200자 이상")
    @Test
    void savePost_WhenTileIsMoreThan200() {
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title(String.join("", Collections.nCopies(201, "t")))
                .build();

        assertThatThrownBy(() -> postService.savePost(postRegistDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content must be up to 200 characters.");
    }

    @DisplayName("savePost 실패 테스트 Title 1000자 이상")
    @Test
    void savePost_WhenContentIsMoreThan1000() {
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title("Test savePost_WhenContentIsMoreThan1000")
                .content(String.join("", Collections.nCopies(1002, "T")))
                .build();

        assertThatThrownBy(() -> postService.savePost(postRegistDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content must be up to 1000 characters.");
    }

    @DisplayName("updatePost 성공 테스트")
    @Test
    void updatePost() {
        // bootrun을 하지 않으면 yml의 상수값을 읽어오지 못해 업데이트가능 날짜를 테스트 할 수 없음
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test updatePost").build();
        post.setCreatedAt(LocalDateTime.now());

        String changeTitle = "Test updated";
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title(changeTitle).build();

        given(postMapper.getPost(anyLong())).willAnswer(invocation -> {
            post.setTitle(changeTitle);
            return post;
        });

        doNothing().when(postMapper).updatePost(any(Post.class));

        //when
        PostUpdateDTO postUpdate = postService.updatePost(postRegistDTO, postId);

        //then
        assertThat(postUpdate.getPostDetailDTO().getTitle()).isEqualTo(changeTitle);
        assertThat(postUpdate.getMessage()).isEqualTo("10 days left");
    }

    @DisplayName("updatePost 실패 테스트 수정 기한 초과")
    @Test
    void updatePost_WhenOverdue() {
        // given
        Long postId = 1L;
        PostRegistDTO postRegist = PostRegistDTO.builder().title("Test updatePost_WhenOverdue").build();
        Post post = Post.builder().title("Test updatePost_WhenOverdue").build();
        post.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0));

        given(postMapper.getPost(postId)).willReturn(post);

        // when
        // then
        assertThatThrownBy(() -> postService.updatePost(postRegist, postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Editing is overdue for post with id " + postId);

    }

    @Test
    void allPostList() {
    }

    @Test
    void postListSearch() {
    }

    @DisplayName("deletePost 성공 테스트")
    @Test
    void deletePost() {
        // mapper 테스트와 완전 동일 하고 모킹 한다면 따로 테스트 할 게 없는데 테스트 해야 하나요?
    }

    @DisplayName("softDeletePost 실패 테스트 이미 softDelete 된 게시물")
    @Test
    void softDeletePost_WhenAlreadyDeleted() {
        //given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test SoftDelete Fail").build();
        post.setDeletedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        //when then
        assertThatThrownBy(() -> postService.softDeletePost(postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("The post with id " + postId + " has already been deleted");
    }

    @DisplayName("getModifiableDate 성공 테스트")
    @Test
    void getModifiableDate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime testDate = LocalDateTime.of(2023, 11, 29, 0, 0);
        long result = ApplicationYamlRead.getModifiableDateValue() - ChronoUnit.DAYS.between(testDate, now);

        //when then
        assertThat(postService.getModifiableDate(testDate)).isEqualTo(result);
    }
}