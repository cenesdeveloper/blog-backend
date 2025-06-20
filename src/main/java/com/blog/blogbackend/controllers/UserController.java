package com.blog.blogbackend.controllers;

import com.blog.blogbackend.dtos.UserDto;
import com.blog.blogbackend.entities.User;
import com.blog.blogbackend.mappers.UserMapper;
import com.blog.blogbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestAttribute UUID uuid) {
        User user = userService.getUserById(uuid);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
