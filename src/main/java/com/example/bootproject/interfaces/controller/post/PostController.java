package com.example.bootproject.interfaces.controller.post;

import com.example.bootproject.domain.post.DeleteType;
import com.example.bootproject.domain.post.Post;
import com.example.bootproject.interfaces.dto.post.PostSearch;
import com.example.bootproject.interfaces.dto.post.PostDetailDTO;
import com.example.bootproject.interfaces.dto.post.PostRegistDTO;
import com.example.bootproject.interfaces.dto.post.PostUpdateDTO;
import com.example.bootproject.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "posts", description = "Post API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "View all posts", description = "View all registered posts.", tags = {"posts"})
    @GetMapping("/api/posts/all")
    public List<Post> allPosts() {
        return postService.allPostList();
    }

    @Operation(summary = "Post View", description = "Search for posts with a specific ID.", tags = {"posts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post view successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDetailDTO.class),
                    examples = @ExampleObject(name = "Get Post",
                            value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"uuid\": \"4e60e67c-af80-11ee-a127-00155dddd5ce\",\n" +
                                    "  \"title\": \"Post Title\",\n" +
                                    "  \"content\": \"Post Contents....\",\n" +
                                    "  \"createdAt\": \"2023-11-29T23:37:16\",\n" +
                                    "  \"updatedAt\": \"2023-11-29T23:37:16\",\n" +
                                    "  \"modifiableDate\": 10\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "Not Found",
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-10T02:17:58.323+00:00\",\n" +
                                    "  \"message\": \"There is no post with id: 10145\",\n" +
                                    "  \"status\": 404\n" +
                                    "}")))})
    @GetMapping("/api/posts/{postId}")
    public PostDetailDTO getPost(@PathVariable("postId") String uuid) {
        return postService.getPost(uuid);
    }

    @Operation(summary = "Post Search", description = "Search for posts that meet the conditions.", tags = {"posts"})
    @GetMapping("/api/posts")
    public List<PostDetailDTO> searchPost(@Parameter(name = "PostSearch", description = "Search criteria") PostSearch search) {
        return postService.postListSearch(search);
    }

    @Operation(summary = "Create post", description = "Register a new post.", tags = {"posts"})
    @ApiResponse(responseCode = "201", description = "Post creation successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDetailDTO.class),
            examples = @ExampleObject(name = "Create",
                    value = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"uuid\": \"4e60e67c-af80-11ee-a127-00155dddd5ce\",\n" +
                            "  \"title\": \"Post Title\",\n" +
                            "  \"content\": \"Post Contents....\",\n" +
                            "  \"createdAt\": \"2023-11-29T23:37:16\",\n" +
                            "  \"updatedAt\": \"2023-11-29T23:37:16\",\n" +
                            "  \"modifiableDate\": 10\n" +
                            "}")))
    @PostMapping("/api/posts")
    public ResponseEntity<PostDetailDTO> insertPost(@Valid @RequestBody PostRegistDTO post) {
        PostDetailDTO postDetail = postService.savePost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(postDetail);
    }

    @Operation(summary = "Update post", description = "Edit the content of a specific post.", tags = {"posts"})
    @PutMapping("/api/posts/{postId}")
    public PostUpdateDTO updatePost(@PathVariable("postId") String uuid, @Valid @RequestBody PostRegistDTO postRegistDTO) {
        return postService.updatePost(postRegistDTO, uuid);
    }

    @Operation(summary = "Delete post", description = "Delete a specific post.", tags = {"posts"})
    @DeleteMapping("/api/posts/{postId}")
    @ApiResponse(responseCode = "404", description = "Post not found",
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Not Found",
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-10T02:17:58.323+00:00\",\n" +
                                    "  \"message\": \"There is no post with id: 10145\",\n" +
                                    "  \"status\": 404\n" +
                                    "}"),
                            @ExampleObject(name = "BadRequest",
                                    value = "{\n" +
                                            "  \"timestamp\": \"2024-01-10T02:17:58.323+00:00\",\n" +
                                            "  \"message\": \"The post with id 10 has already been deleted\",\n" +
                                            "  \"status\": 400\n" +
                                            "}")
                    }))
    public void deletePost(@PathVariable("postId") String uuid, @RequestParam("deleteType") DeleteType deleteType) {
        if (deleteType.equals(DeleteType.HARD)) {
            postService.deletePost(uuid);
        } else {
            postService.softDeletePost(uuid);
        }
    }
}