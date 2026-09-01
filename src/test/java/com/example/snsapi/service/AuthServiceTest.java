package com.example.snsapi.service;

import com.example.snsapi.dto.LoginRequest;
import com.example.snsapi.dto.LoginResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.exception.InvalidCredentialsException;
import com.example.snsapi.fixture.UserFixture;
import com.example.snsapi.repository.UserRepository;
import com.example.snsapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private User user;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp(){
        user = UserFixture.user();
    }

    @Test
    void login_success(){
        LoginRequest request = new LoginRequest(user.getUsername(), "password");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getHashedPassword())).thenReturn(true);
        when(jwtService.createToken(user.getId())).thenReturn("jwt-token");
        // Act
        LoginResponse response = authService.login(request);

        assertEquals("jwt-token", response.token());
        assertEquals("bearer", response.tokenType());

        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches(request.password(), user.getHashedPassword());
        verify(jwtService).createToken(user.getId());
    }

    @Test
    void login_usernameNotFound(){
        LoginRequest request = new LoginRequest("dummy user", "password");
        when(userRepository.findByUsername("dummy user")).thenReturn(Optional.empty());
        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername("dummy user");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).createToken(any(UUID.class));
    }

    @Test
    void login_invalidPassword(){
        LoginRequest request = new LoginRequest(user.getUsername(), "invalidpassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getHashedPassword())).thenReturn(false);
        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches(request.password(), user.getHashedPassword());
        verify(jwtService, never()).createToken(any(UUID.class));
    }
}
