package com.example.bootproject.service.post;

import com.example.bootproject.BootProjectApplication;
import com.example.bootproject.TestConfig;
import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import com.example.bootproject.interfaces.dto.post.PostRegistDTO;
import com.example.bootproject.interfaces.dto.post.PostUpdateDTO;
import com.example.bootproject.mapper.post.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    // Do not use DB by mocking
    @Mock
    private PostMapper postMapper;

    // Going to test this by adding a smarter Mock.
    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(postService, "modifiableDateValue", 10);

    }

    @DisplayName("getRawPost 성공 테스트")
    @Test
    void getRawPost() {
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).uuid("4e610b8f-af80-11ee-a127-00155dddd5ce").title("Test getRawPost").build();

        when(postMapper.getPostById(postId)).thenReturn(post);

        // when
        Post rawPost = postService.getRawPost(postId);

        // then
        assertThat(rawPost.getId()).isEqualTo(1L);
        assertThat(rawPost.getTitle()).isEqualTo("Test getRawPost");
    }

    @DisplayName("getRawPost 실패 테스트 데이터 없는 경우")
    @Test
    void getRawPost_WhenPostDoseNotExist() throws Exception {
        // given
        Long postId = 1L;
        given(postMapper.getPostById(postId)).willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> postService.getRawPost(1L))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("getPost 성공 테스트")
    @Test
    void getPost() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder().id(postId).title("Test getPost").build();
        post.setCreatedAt(LocalDateTime.now());
        post.setUuid(uuid);

        given(postMapper.getPostByUUID(uuid)).willReturn(post);

        // when
        PostDetailDTO postDetail = postService.getPost(uuid);

        // then
        assertThat(postDetail.getUuid()).isEqualTo("4e610b8f-af80-11ee-a127-00155dddd5ce");
        assertThat(postDetail.getTitle()).isEqualTo("Test getPost");
    }

    @DisplayName("getPost 실패 테스트 softDelete 된 경우")
    @Test
    void getPost_WhenPostAlreadySoftDeleted() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder().id(postId).title("Test getPost_WhenPostAlreadySoftDeleted").build();
        post.setDeletedAt(LocalDateTime.now());
        post.setUuid(uuid);

        given(postMapper.getPostByUUID(uuid)).willReturn(post);

        // when
        // then
        assertThatThrownBy(() -> postService.getPost("4e610b8f-af80-11ee-a127-00155dddd5ce"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Post with id " + "4e610b8f-af80-11ee-a127-00155dddd5ce" + " is already deleted");
    }

    @DisplayName("getPost 실패 테스트 데이터 없는 경우")
    @Test
    void getPost_WhenPostDoseNotExist() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        given(postMapper.getPostByUUID(uuid)).willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> postService.getPost("4e610b8f-af80-11ee-a127-00155dddd5ce"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("There is no post with id: " + "4e610b8f-af80-11ee-a127-00155dddd5ce");
    }

    @DisplayName("savePost 성공 테스트")
    @Test
    void savePost() {
        // given
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        PostRegistDTO postRegist = PostRegistDTO.builder().title("Test savePost").build();
        Post post = Post.builder().id(1L).title("Test savePost").build();
        post.setCreatedAt(LocalDateTime.now());
        post.setUuid(uuid);

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
            ReflectionTestUtils.setField(returnPost, "uuid", "4e610b8f-af80-11ee-a127-00155dddd5ce");
            return null;
        }).when(postMapper).insertPost(any(Post.class));

        given(postMapper.getPostByUUID(anyString())).willReturn(post);

        // when
        PostDetailDTO postDetailDTO = postService.savePost(postRegist);

        // then
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test savePost");
    }

    @DisplayName("updatePost 성공 테스트")
    @Test
    void updatePost() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder().id(postId).title("Test updatePost").build();
        post.setCreatedAt(LocalDateTime.now());
        post.setUuid(uuid);

        String changeTitle = "Test updated";
        PostDetailDTO postDetailDTO = PostDetailDTO.builder().title(changeTitle).modifiableDate(1L).build();
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title(changeTitle).build();

        given(postMapper.getPostByUUID(uuid)).willAnswer(invocation -> {
            post.setTitle(changeTitle);
            return post;
        });

        doNothing().when(postMapper).updatePost(post);

        // when
        PostUpdateDTO postUpdate = postService.updatePost(postRegistDTO, uuid);

        // then
        assertThat(postUpdate.getPostDetailDTO().getTitle()).isEqualTo(changeTitle);
        assertThat(postUpdate.getMessage()).isEqualTo("10 days left");
    }

    @DisplayName("updatePost 성공 테스트")
    @Test
    void updatePosts() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title("Test updatePost").build();
        String originalTitle = "Test updatePost";
        String changeTitle = "Test updated";

        // Post Object Mocking
        Post mockPost = mock(Post.class);
        when(mockPost.getCreatedAt()).thenReturn(LocalDateTime.now());

        // PostDetailDTO Object Mocking
        PostDetailDTO mockPostDetailDTO = mock(PostDetailDTO.class);
        when(mockPostDetailDTO.getTitle()).thenReturn(changeTitle);
        when(mockPostDetailDTO.getModifiableDate()).thenReturn(1L);

        // postMapper.getPost(postId)가 호출될 때 mockPost 반환
        when(postMapper.getPostByUUID(uuid)).thenReturn(mockPost);
        when(postService.getPost(uuid)).thenReturn(mockPostDetailDTO);

        // postService.updatePost(...)를 호출할 때 mockPostDetailDTO 반환
        when(postService.updatePost(postRegistDTO, uuid)).thenReturn(PostUpdateDTO.builder().postDetailDTO(mockPostDetailDTO).message("10 days left").build());

        // when
        PostUpdateDTO postUpdate = postService.updatePost(PostRegistDTO.builder().title(changeTitle).build(), uuid);

        // then
        assertThat(postUpdate.getPostDetailDTO().getTitle()).isEqualTo(changeTitle);
        assertThat(postUpdate.getMessage()).isEqualTo("10 days left");
    }

    @DisplayName("updatePost 실패 테스트 수정 기한 초과")
    @Test
    void updatePost_WhenOverdue() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        PostRegistDTO postRegist = PostRegistDTO.builder().title("Test updatePost_WhenOverdue").build();
        Post post = Post.builder().title("Test updatePost_WhenOverdue").build();
        post.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0));
        post.setUuid(uuid);

        given(postMapper.getPostByUUID(uuid)).willReturn(post);

        // when then
        assertThatThrownBy(() -> postService.updatePost(postRegist, "4e610b8f-af80-11ee-a127-00155dddd5ce"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Editing is overdue for post with id " + "4e610b8f-af80-11ee-a127-00155dddd5ce");

    }

    @DisplayName("deletePost 성공 테스트")
    @Test
    void deletePost() {
    }

    @DisplayName("softDeletePost 실패 테스트 이미 softDelete 된 게시물")
    @Test
    void softDeletePost_WhenAlreadyDeleted() {
        // given
        Long postId = 1L;
        String uuid = "4e610b8f-af80-11ee-a127-00155dddd5ce";
        Post post = Post.builder().id(postId).title("Test SoftDelete Fail").build();
        post.setDeletedAt(LocalDateTime.now());
        post.setUuid(uuid);

        given(postMapper.getPostByUUID(uuid)).willReturn(post);

        // when then
        assertThatThrownBy(() -> postService.softDeletePost("4e610b8f-af80-11ee-a127-00155dddd5ce"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("The post with id 4e610b8f-af80-11ee-a127-00155dddd5ce has already been deleted");
    }

    @DisplayName("getModifiableDate 성공 테스트")
    @Test
    void getModifiableDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime testDate = LocalDateTime.of(2023, 11, 29, 0, 0);
        long result = 10 - ChronoUnit.DAYS.between(testDate, now);
        System.out.println(result);
        System.out.println(postService.getModifiableDate(testDate));
        // when then
        assertThat(postService.getModifiableDate(testDate)).isEqualTo(result);
    }
}