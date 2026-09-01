package com.example.snsapi.service;

import com.example.snsapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "this-is-a-very-long-secret-key-for-testing-purposes";

    @BeforeEach
    void setUp(){
        jwtService = new JwtService(SECRET);
    }

    

}
