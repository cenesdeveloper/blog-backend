package com.blog.blogbackend.services.impl;

import com.blog.blogbackend.domain.dtos.UserDto;
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.repositories.UserRepository;
import com.blog.blogbackend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalStateException("Email is already registered");
        }

        User user = new User();
        user.setName(userDto.getName());  // <-- now taken from request
        user.setEmail(userDto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User saved = userRepository.save(user);

        return UserDto.builder()
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }
}
