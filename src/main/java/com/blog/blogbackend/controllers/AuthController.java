package com.blog.blogbackend.controllers;

import com.blog.blogbackend.domain.dtos.AuthResponse;
import com.blog.blogbackend.domain.dtos.LoginRequest;
import com.blog.blogbackend.domain.dtos.UserDto;
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.repositories.UserRepository;
import com.blog.blogbackend.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping(path="/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest  loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return  ResponseEntity.ok(authResponse);
    }

    @PostMapping(path="/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        // 1) conflict if email already exists
        Optional<User> existing = userRepository.findByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 2) create user (use injected PasswordEncoder)
        User saved = userRepository.save(
                User.builder()
                        .name("Test User")                      // or pull from DTO later
                        .email(userDto.getEmail())
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .build()
        );

        // 3) build safe response (no password fields)
        UserDto response = UserDto.builder()
                .email(saved.getEmail())
                .password(null)
                .matchingPassword(null)
                .build();

        // 4) return 201 Created (+ Location header to where this resource would live)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
