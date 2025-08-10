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
        // 1) Functional check: password must match
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // 2) Uniqueness check
        if (userRepository.existsByEmail(userDto.getEmail().toLowerCase())) {
            throw new IllegalStateException("Email is already registered");
        }

        // 3) Map DTO -> Entity and hash password
        User user = new User();
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 4) Save user
        User saved = userRepository.save(user);

        // 5) Map back to safe DTO (omit passwords)
        return UserDto.builder()
                .email(saved.getEmail())
                .build();
    }
}
