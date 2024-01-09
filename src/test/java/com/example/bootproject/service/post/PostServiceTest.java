package com.example.bootproject.service.post;

import com.example.bootproject.exception.BadRequestException;
import com.example.bootproject.exception.NotFoundException;
import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import com.example.bootproject.interfaces.dto.post.PostRegistDTO;
import com.example.bootproject.interfaces.dto.post.PostUpdateDTO;
import com.example.bootproject.mapper.post.mapper.PostMapper;
import com.example.bootproject.service.post.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

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

    @Value("${constant-data.modifiable-date-value}")
    private int modifiableDateValue;

    @DisplayName("getRawPost 성공 테스트")
    @Test
    void getRawPost() {
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getRawPost").build();

        given(postMapper.getPost(postId)).willReturn(post);

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

        given(postMapper.getPost(postId)).willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> postService.getRawPost(postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("There is no post with id: " + postId);
    }

    @DisplayName("getRawPost 실패 테스트 ID 값이 null인 경우")
    @Test
    void getRawPost_WhenPostIdIsNull() throws Exception {
        // given
        Long postId = null;

        // when
        // then
        assertThatThrownBy(() -> postService.getRawPost(postId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID cannot be null");
    }

    @DisplayName("getPost 성공 테스트")
    @Test
    void getPost() {
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getPost").build();
        post.setCreatedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        // when
        PostDetailDTO postDetail = postService.getPost(postId);

        // then
        assertThat(postDetail.getId()).isEqualTo(1L);
        assertThat(postDetail.getTitle()).isEqualTo("Test getPost");
    }

    @DisplayName("getPost 실패 테스트 softDelete 된 경우")
    @Test
    void getPost_WhenPostAlreadySoftDeleted() {
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test getPost_WhenPostAlreadySoftDeleted").build();
        post.setDeletedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        // when
        // then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Post with id " + postId + " is already deleted");
    }

    @DisplayName("getPost 실패 테스트 데이터 없는 경우")
    @Test
    void getPost_WhenPostDoseNotExist() {
        // given
        Long postId = 1L;
        given(postMapper.getPost(postId)).willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("There is no post with id: " + postId);
    }

    @DisplayName("savePost 성공 테스트")
    @Test
    void savePost() {
        // given
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

        // when
        PostDetailDTO postDetailDTO = postService.savePost(postRegist);

        // then
        assertThat(postDetailDTO.getTitle()).isEqualTo("Test savePost");
    }

    @DisplayName("savePost 실패 테스트 Title 200자 이상")
    @Test
    void savePost_WhenTileIsMoreThan200() {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title(String.join("", Collections.nCopies(201, "t")))
                .build();

        // when then
        assertThatThrownBy(() -> postService.savePost(postRegistDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title must be up to 200 characters.");
    }

    @DisplayName("savePost 실패 테스트 Title 1000자 이상")
    @Test
    void savePost_WhenContentIsMoreThan1000() {
        // given
        PostRegistDTO postRegistDTO = PostRegistDTO.builder()
                .title("Test savePost_WhenContentIsMoreThan1000")
                .content(String.join("", Collections.nCopies(1002, "T")))
                .build();

        // when then
        assertThatThrownBy(() -> postService.savePost(postRegistDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content must be up to 1000 characters.");
    }

    @DisplayName("updatePost 성공 테스트")
    @Test
    void updatePost() {
        // given
        Long postId = 1L;
        Post post = Post.builder().id(postId).title("Test updatePost").build();
        post.setCreatedAt(LocalDateTime.now());

        String changeTitle = "Test updated";
        PostDetailDTO postDetailDTO = PostDetailDTO.builder().title(changeTitle).modifiableDate(1L).build();
        PostRegistDTO postRegistDTO = PostRegistDTO.builder().title(changeTitle).build();

        given(postMapper.getPost(postId)).willAnswer(invocation -> {
            post.setTitle(changeTitle);
            return post;
        });

        doNothing().when(postMapper).updatePost(post);

        // when
        PostUpdateDTO postUpdate = postService.updatePost(postRegistDTO, postId);

        // then
        assertThat(postUpdate.getPostDetailDTO().getTitle()).isEqualTo(changeTitle);
        assertThat(postUpdate.getMessage()).isEqualTo("10 days left");
    }
    @DisplayName("updatePost 성공 테스트")
    @Test
    void updatePosts() {
        // given
        Long postId = 1L;
        String originalTitle = "Test updatePost";
        String changeTitle = "Test updated";

        // Post Object Mocking
        Post mockPost = mock(Post.class);
        when(mockPost.getId()).thenReturn(postId);
        when(mockPost.getTitle()).thenReturn(originalTitle, changeTitle); // 첫 호출에서는 원본 제목, 이후 변경된 제목 반환

        // PostDetailDTO Object Mocking
        PostDetailDTO mockPostDetailDTO = mock(PostDetailDTO.class);
        when(mockPostDetailDTO.getTitle()).thenReturn(changeTitle);
        when(mockPostDetailDTO.getModifiableDate()).thenReturn(1L);

        // postMapper.getPost(postId)가 호출될 때 mockPost 반환
        given(postMapper.getPost(postId)).willReturn(mockPost);

        // postService.updatePost(...)를 호출할 때 mockPostDetailDTO 반환
        given(postService.updatePost(any(PostRegistDTO.class), eq(postId))).willReturn(PostUpdateDTO.builder().postDetailDTO(mockPostDetailDTO).message("10 days left").build());

        // when
        PostUpdateDTO postUpdate = postService.updatePost(PostRegistDTO.builder().title(changeTitle).build(), postId);

        // then
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

        // when then
        assertThatThrownBy(() -> postService.updatePost(postRegist, postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Editing is overdue for post with id " + postId);

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
        Post post = Post.builder().id(postId).title("Test SoftDelete Fail").build();
        post.setDeletedAt(LocalDateTime.now());

        given(postMapper.getPost(postId)).willReturn(post);

        // when then
        assertThatThrownBy(() -> postService.softDeletePost(postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("The post with id " + postId + " has already been deleted");
    }

    @DisplayName("getModifiableDate 성공 테스트")
    @Test
    void getModifiableDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime testDate = LocalDateTime.of(2023, 11, 29, 0, 0);
        long result = modifiableDateValue - ChronoUnit.DAYS.between(testDate, now);

        // when then
        assertThat(postService.getModifiableDate(testDate)).isEqualTo(result);
    }
}