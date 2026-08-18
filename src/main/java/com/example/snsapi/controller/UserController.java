package com.example.snsapi.controller;

import com.example.snsapi.dto.UserCreateRequest;
import com.example.snsapi.dto.UserResponse;
import com.example.snsapi.entity.User;
import com.example.snsapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create User")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/me")
    @Operation(summary = "Get My Information")
    public UserResponse getMe(@AuthenticationPrincipal User user){
        return userService.toResponse(user);
    }
}
