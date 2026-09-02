package com.example.snsapi.service;

import com.example.snsapi.exception.InvalidCredentialsException;
import com.example.snsapi.exception.InvalidTokenException;
import com.example.snsapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class JwtServiceTest {

    private JwtService jwtService;
    private Clock clock;

    private static final String SECRET = "this-is-a-very-long-secret-key-for-testing-purposes";

    @BeforeEach
    void setUp(){
        clock = Clock.fixed(
                Instant.parse("2026-01-01T00:00:00Z"),
                ZoneOffset.UTC
        );
        jwtService = new JwtService(SECRET, clock);  // インスタンス化のときにSecretKey必要なので事前にセット
    }

    private JwtService createJwtService(Instant instant) {
        Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
        return new JwtService(SECRET, clock);
    }

    @Test
    void createToken_success(){
        UUID userId = UUID.randomUUID();
        // Act
        String token = jwtService.createToken(userId);
        // Assert
        UUID decodedUserId = jwtService.decodeToken(token);
        assertEquals(userId, decodedUserId);
    }

    @Test
    void decodeToken_invalidJwt(){
        String token = "this.is.not.a.valid.jwt";

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> jwtService.decodeToken(token));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void decodeToken(){
        UUID userId = UUID.randomUUID();
        JwtService issuingJwtService = createJwtService(Instant.parse("2026-01-01T00:00:00Z"));
        String token = issuingJwtService.createToken(userId);
        JwtService expiredJwtService = createJwtService(Instant.parse("2026-01-01T00:31:00Z"));

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> expiredJwtService.decodeToken(token));
        assertEquals("Token has expired", exception.getMessage());
    }

}
