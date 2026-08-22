package com.example.snsapi.controller;

import com.example.snsapi.dto.PageResponse;
import com.example.snsapi.dto.PostCreateRequest;
import com.example.snsapi.dto.PostResponse;
import com.example.snsapi.dto.PostUpdateRequest;
import com.example.snsapi.entity.User;
import com.example.snsapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Operation(summary = "Create Post")
    public PostResponse createPost(@Valid @RequestBody PostCreateRequest request,
                                   @AuthenticationPrincipal User user){
        return postService.createPost(request, user);
    }

    @GetMapping
    @Operation(summary = "Get Posts")
    public PageResponse<PostResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "50") int size){
        return postService.getPosts(page, size);
    }

    @GetMapping("/me")
    @Operation(summary = "Get My Posts")
    public PageResponse<PostResponse> getMyPosts(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "50") int size,
                                                 @AuthenticationPrincipal User user){
        return postService.getMyPosts(page, size, user);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get Single Post")
    public PostResponse getSinglePost(@PathVariable UUID postId){
        return postService.getSinglePost(postId);
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "Update Post")
    public PostResponse updatePost(@PathVariable UUID postId,
                                   @RequestBody PostUpdateRequest request,
                                   @AuthenticationPrincipal User user){
        return postService.updatePost(postId, request, user);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete Post")
    public Map<String, String> deletePost(@PathVariable UUID postId,
                                          @AuthenticationPrincipal User user){
        return postService.deletePost(postId, user);
    }
}
