package com.blog.blogbackend.services;

import com.blog.blogbackend.domain.CreatePostRequest;
import com.blog.blogbackend.domain.UpdatePostRequest;
import com.blog.blogbackend.domain.entities.Post;
import com.blog.blogbackend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(List<UUID> categoryIds, List<UUID> tagIds); // keeps your old use
    List<Post> getPublishedPosts(List<UUID> categoryIds, List<UUID> tagIds); // NEW
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);

    // existing open update/delete (kept for internal use)
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);

    // NEW: authZ-enforced variants used by controller
    Post getPostVisibleTo(UUID id, UUID requesterId);
    Post updatePostAuthorized(UUID id, UpdatePostRequest updatePostRequest, UUID requesterId);
    void deletePostAuthorized(UUID id, UUID requesterId);
}
