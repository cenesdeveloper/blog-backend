package com.blog.blogbackend.services;

import com.blog.blogbackend.domain.dtos.UserDto;
import com.blog.blogbackend.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    UserDto register(UserDto userDto);
}
