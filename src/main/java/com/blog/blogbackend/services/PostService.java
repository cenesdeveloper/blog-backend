package com.blog.blogbackend.services;

import com.blog.blogbackend.CreatePostRequest;
import com.blog.blogbackend.UpdatePostRequest;
import com.blog.blogbackend.entities.Post;
import com.blog.blogbackend.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
}
