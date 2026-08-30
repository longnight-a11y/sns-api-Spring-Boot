package com.example.snsapi.service;

import com.example.snsapi.dto.PageResponse;
import com.example.snsapi.dto.PostCreateRequest;
import com.example.snsapi.dto.PostResponse;
import com.example.snsapi.entity.Post;
import com.example.snsapi.entity.User;
import com.example.snsapi.fixture.PostFixture;
import com.example.snsapi.fixture.UserFixture;
import com.example.snsapi.repository.PostRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    private Post post;
    private User user;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp(){
        user = UserFixture.user();
        post = PostFixture.post(user);
    }

    @Test
    void createPost_success(){
        PostCreateRequest request = PostFixture.request();
        // Arrange
        when(postRepository.save(any(Post.class))).thenReturn(post);
        // Act
        PostResponse response = postService.createPost(request, user);
        // Assert
        assertEquals("Test Post", response.title());

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());

        Post savedPost = captor.getValue();
        assertEquals("Test Post", savedPost.getTitle());
        assertEquals("content", savedPost.getContent());
    }

    @Test
    void getPosts_success(){
        Page<Post> page = new PageImpl<>(List.of(post));

        when(postRepository.findAllWithUser(any(PageRequest.class))).thenReturn(page);
        // Act
        PageResponse<PostResponse> response = postService.getPosts(1, 10);

        verify(postRepository).findAllWithUser(any(PageRequest.class));
        assertEquals(1, response.total());
        assertEquals(1, response.items().size());
        assertEquals(post.getId(), response.items().getFirst().id());
    }


    // getMyPosts

    // getSinglePost
}
