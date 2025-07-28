package com.blog.blogbackend.controllers;

import com.blog.blogbackend.domain.CreatePostRequest;
import com.blog.blogbackend.domain.UpdatePostRequest;
import com.blog.blogbackend.domain.dtos.CreatePostRequestDto;
import com.blog.blogbackend.domain.dtos.PostDto;
import com.blog.blogbackend.domain.dtos.UpdatePostRequestDto;
import com.blog.blogbackend.domain.entities.Post;
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.mappers.PostMapper;
import com.blog.blogbackend.repositories.UserRepository;
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
    public ResponseEntity<List<PostDto>>  getAllPosts(
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) UUID tagId) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toPostDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getAllDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toPostDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userID)  {

        User loggedInUser = userService.getUserById(userID);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost =  postService.createPost(loggedInUser, createPostRequest);
        PostDto postDto = postMapper.toPostDto(createdPost);
        return new  ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toPostDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id
    ) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toPostDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<PostDto> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
