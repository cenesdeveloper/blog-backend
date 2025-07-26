package com.blog.blogbackend.services;

import org.springframework.security.core.userdetails.UserDetails;


public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails user);
    UserDetails validateToken(String token);
}
