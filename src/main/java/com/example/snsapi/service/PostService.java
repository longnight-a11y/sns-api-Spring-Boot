package com.example.snsapi.service;

import com.example.snsapi.dto.PageResponse;
import com.example.snsapi.dto.PostCreateRequest;
import com.example.snsapi.dto.PostResponse;
import com.example.snsapi.dto.UserResponse;
import com.example.snsapi.entity.Post;
import com.example.snsapi.entity.User;
import com.example.snsapi.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest request, User user){

        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        post.setUser(user);
        return toResponse(postRepository.save(post));
    }

    public PageResponse<PostResponse> getPosts(int page, int size){

        Page<Post> result = postRepository.findAllWithUser(PageRequest.of(page - 1, size));
        List<PostResponse> items = result.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(items,(int) result.getTotalElements(), page, size);
    }


    public PostResponse toResponse(Post post){
        User user = post.getUser();
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(),
                new UserResponse(user.getId(), user.getUsername()));
    }
}
