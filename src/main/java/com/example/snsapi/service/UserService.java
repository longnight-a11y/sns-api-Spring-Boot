package com.example.snsapi.service;

import com.example.snsapi.dto.UserCreateRequest;
import com.example.snsapi.dto.UserResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.exception.ConflictException;
import com.example.snsapi.repository.UserRepository;
import com.example.snsapi.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request){

        if(userRepository.existsByUsername(request.username())){
            throw new ConflictException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setHashedPassword(passwordEncoder.encode(request.password()));
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse getMe(User currentUser){
        return toResponse(currentUser);
    }


    public UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getUsername());
    }
}
