package com.example.snsapi.service;

import com.example.snsapi.dto.LoginRequest;
import com.example.snsapi.dto.LoginResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.exception.InvalidTokenException;
import com.example.snsapi.repository.UserRepository;
import com.example.snsapi.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService (UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request){

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidTokenException("Invalid username or password"));
        if(!passwordEncoder.matches(request.password(), user.getHashedPassword())){
            throw new InvalidTokenException("Invalid username or password");
        }
        return new LoginResponse(jwtService.createToken(user.getId()), "bearer");
    }
}
