package com.example.snsapi.service;

import com.example.snsapi.dto.UserCreateRequest;
import com.example.snsapi.dto.UserResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.exception.ConflictException;
import com.example.snsapi.fixture.UserFixture;
import com.example.snsapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserCreateRequest request;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        request = UserFixture.request();
    }

    @Test
    void createUser_success(){
        // Arrange
        User saved = new User();
        saved.setUsername("Mikasa");
        saved.setHashedPassword("hashedpassword");
        when(userRepository.existsByUsername("Mikasa")).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(saved.getHashedPassword());
        when(userRepository.save(any(User.class))).thenReturn(saved);
        // Act
        UserResponse response = userService.createUser(request);
        // Assert
        assertEquals("Mikasa", response.username());  // check whether the service returns proper response

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        // check whether userRepository.save() is called, and captor the saved contents
        verify(userRepository).save(captor.capture());

        User user = captor.getValue();
        assertEquals("Mikasa", user.getUsername());  // check whether userRepository saved proper data
        assertEquals("hashedpassword", user.getHashedPassword());

        verify(userRepository).existsByUsername("Mikasa");
        verify(passwordEncoder).encode("testpass");
    }

    @Test
    void createUser_userAlreadyExists(){
        when(userRepository.existsByUsername("Mikasa")).thenReturn(true);
        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> userService.createUser(request));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).existsByUsername("Mikasa"); // check whether userRepository.existsByUsername() is called
        // methods below are what never should be called
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
