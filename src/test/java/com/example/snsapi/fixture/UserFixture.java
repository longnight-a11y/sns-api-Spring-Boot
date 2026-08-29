package com.example.snsapi.fixture;

import com.example.snsapi.dto.UserCreateRequest;
import com.example.snsapi.entity.User;

import java.util.UUID;

public final class UserFixture {

    private UserFixture(){}  // prevents instantiation because all methods here are static

    public static User user(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("Mikasa");
        user.setHashedPassword("testpass");
        return user;
    }

    public static User user(String username){
        User user = user();
        user.setUsername(username);
        return user;
    }

    public static UserCreateRequest request(){
        return new UserCreateRequest("Mikasa", "testpass");
    }
}
