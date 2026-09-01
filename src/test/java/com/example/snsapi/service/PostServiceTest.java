package com.example.snsapi.service;

import com.example.snsapi.dto.PageResponse;
import com.example.snsapi.dto.PostCreateRequest;
import com.example.snsapi.dto.PostResponse;
import com.example.snsapi.dto.PostUpdateRequest;
import com.example.snsapi.entity.Post;
import com.example.snsapi.entity.User;
import com.example.snsapi.exception.AccessDeniedException;
import com.example.snsapi.exception.InvalidRequestException;
import com.example.snsapi.exception.ResourceNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Test
    void getMyPosts_success(){
        Page<Post> page = new PageImpl<>(List.of(post));
        UUID userId = user.getId();

        when(postRepository.findByUserId(eq(userId), any(PageRequest.class))).thenReturn(page);
        // Act
        PageResponse<PostResponse> response = postService.getMyPosts(1, 10, user);

        verify(postRepository).findByUserId(eq(userId), any(PageRequest.class));
        assertEquals(1, response.total());
        assertEquals(1, response.items().size());
        assertEquals(post.getId(), response.items().getFirst().id());
    }

    @Test
    void getSinglePost_success(){
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        // Act
        PostResponse response = postService.getSinglePost(post.getId());

        verify(postRepository).findByIdWithUser(post.getId());
        assertEquals("Test Post", response.title());
        assertEquals("content", response.content());
    }

    @Test
    void getSinglePost_notFound(){
        UUID id = UUID.randomUUID();
        when(postRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> postService.getSinglePost(id));
        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findByIdWithUser(id);
    }

    @Test
    void updatePost_success_titleOnly(){
        PostUpdateRequest request = new PostUpdateRequest("Updated Title", null);
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        PostResponse response = postService.updatePost(post.getId(), request, user);

        assertEquals("Updated Title", response.title());
        assertEquals("content", response.content());
    }

    @Test
    void updatePost_success_contentOnly(){
        PostUpdateRequest request = new PostUpdateRequest(null, "Updated Content");
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        PostResponse response = postService.updatePost(post.getId(), request, user);

        assertEquals("Test Post", response.title());
        assertEquals("Updated Content", response.content());
    }

    @Test
    void updatePost_success_all(){
        PostUpdateRequest request = new PostUpdateRequest("Updated Title", "Updated Content");
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        PostResponse response = postService.updatePost(post.getId(), request, user);

        assertEquals("Updated Title", response.title());
        assertEquals("Updated Content", response.content());
    }

    @Test
    void updatePost_notFound(){
        PostUpdateRequest request = new PostUpdateRequest("Updated Title", "Updated Content");
        UUID id = UUID.randomUUID();
        when(postRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(id, request, user));
        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findByIdWithUser(id);
    }

    @Test
    void updatePost_notAuthorized(){
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        PostUpdateRequest request = new PostUpdateRequest("Updated Title", "Updated Content");
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> postService.updatePost(post.getId(), request, otherUser));
        assertEquals("Not authorized to update this task", exception.getMessage());
        verify(postRepository).findByIdWithUser(post.getId());
    }

    @Test
    void updatePost_noFieldProvided(){
        PostUpdateRequest request = new PostUpdateRequest(null, null);
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> postService.updatePost(post.getId(), request, user));
        assertEquals("At least one field must be provided", exception.getMessage());
        verify(postRepository).findByIdWithUser(post.getId());
    }

    @Test
    void updatePost_blankTitle(){
        PostUpdateRequest request = new PostUpdateRequest(" ", null);
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> postService.updatePost(post.getId(), request, user));
        assertEquals("Title must not be blank", exception.getMessage());
        verify(postRepository).findByIdWithUser(post.getId());
    }

    @Test
    void updatePost_blankContent(){
        PostUpdateRequest request = new PostUpdateRequest(null, " ");
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> postService.updatePost(post.getId(), request, user));
        assertEquals("Content must not be blank", exception.getMessage());
        verify(postRepository).findByIdWithUser(post.getId());
    }

    @Test
    void deletePost_success(){
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));
        // Act
        postService.deletePost(post.getId(), user);

        verify(postRepository).delete(post);
    }

    @Test
    void deletePost_notFound(){
        UUID id = UUID.randomUUID();
        when(postRepository.findByIdWithUser(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> postService.deletePost(id, user));
        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findByIdWithUser(id);
    }

    @Test
    void deletePost_notAuthorized(){
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        when(postRepository.findByIdWithUser(post.getId())).thenReturn(Optional.of(post));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> postService.deletePost(post.getId(), otherUser));
        assertEquals("Not authorized to delete this task", exception.getMessage());
        verify(postRepository).findByIdWithUser(post.getId());
        verify(postRepository, never()).delete(any(Post.class));
    }
}
