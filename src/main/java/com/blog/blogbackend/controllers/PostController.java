package com.blog.blogbackend.controllers;

import com.blog.blogbackend.domain.CreatePostRequest;
import com.blog.blogbackend.domain.UpdatePostRequest;
import com.blog.blogbackend.domain.dtos.CreatePostRequestDto;
import com.blog.blogbackend.domain.dtos.PostDto;
import com.blog.blogbackend.domain.dtos.UpdatePostRequestDto;
import com.blog.blogbackend.domain.entities.Post;
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.mappers.PostMapper;
import com.blog.blogbackend.services.PostService;
import com.blog.blogbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path ="api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(value = "categoryId", required = false) List<UUID> categoryIds,
            @RequestParam(value = "tagId", required = false) List<UUID> tagIds
    ) {
        List<Post> posts = postService.getPublishedPosts(categoryIds, tagIds);
        List<PostDto> postDtos = posts.stream().map(postMapper::toPostDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    // OWNER DRAFTS: requires logged-in user
    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getAllDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toPostDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    // CREATE: author forced to current user (already correct)
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId)  {

        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost =  postService.createPost(loggedInUser, createPostRequest);
        PostDto postDto = postMapper.toPostDto(createdPost);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    // READ SINGLE: allow if PUBLISHED, or DRAFT only if owner/admin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id,
            @RequestAttribute(required = false) UUID userId
    ) {
        Post post = postService.getPostVisibleTo(id, userId);
        PostDto postDto = postMapper.toPostDto(post);
        return ResponseEntity.ok(postDto);
    }

    // UPDATE: only owner or admin
    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto,
            @RequestAttribute UUID userId) {

        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePostAuthorized(id, updatePostRequest, userId);
        PostDto updatedPostDto = postMapper.toPostDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    // DELETE: only owner or admin
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id,
            @RequestAttribute UUID userId) {

        postService.deletePostAuthorized(id, userId);
        return ResponseEntity.noContent().build();
    }
}
