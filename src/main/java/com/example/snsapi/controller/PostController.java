package com.example.snsapi.controller;

import com.example.snsapi.dto.PageResponse;
import com.example.snsapi.dto.PostCreateRequest;
import com.example.snsapi.dto.PostResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
