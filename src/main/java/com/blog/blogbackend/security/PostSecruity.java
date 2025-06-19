package com.blog.blogbackend.security;

import com.blog.blogbackend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("postSecurity") // Allows use in @PreAuthorize
@RequiredArgsConstructor
public class PostSecruity {
    private final PostRepository postRepository;

    public boolean isOwner(Authentication authentication, UUID postId) {
        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        UUID currentUserId = userDetails.getUser().getId();

        return postRepository.findById(postId)
                .map(post -> post.getAuthor().getId().equals(currentUserId))
                .orElse(false);
    }
}
