package com.example.snsapi.fixture;

import com.example.snsapi.entity.Post;
import com.example.snsapi.entity.User;

import java.util.UUID;

public class PostFixture {

    private PostFixture(){}

    public static Post post(User user){
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setTitle("Test Post");
        post.setContent("content");
        post.setUser(user);
        return post;
    }
}
