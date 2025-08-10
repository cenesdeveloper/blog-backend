package com.blog.blogbackend.controllers;

import com.blog.blogbackend.domain.dtos.AuthResponse;
import com.blog.blogbackend.domain.dtos.LoginRequest;
import com.blog.blogbackend.domain.dtos.UserDto;
import com.blog.blogbackend.services.AuthenticationService;
import com.blog.blogbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

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
        UserDto created = userService.register(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
