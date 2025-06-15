package com.blog.blogbackend.services;

import com.blog.blogbackend.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
