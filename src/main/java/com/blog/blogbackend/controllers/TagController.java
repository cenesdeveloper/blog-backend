package com.blog.blogbackend.controllers;

import com.blog.blogbackend.dtos.CreateTagsRequest;
import com.blog.blogbackend.dtos.TagResponse;
import com.blog.blogbackend.entities.Tag;
import com.blog.blogbackend.mappers.TagMapper;
import com.blog.blogbackend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagResponse> tagResponses =  tags.stream().map(tagMapper::tagToTagResponse)
                .toList();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest) {
        List<Tag> savedTags =  tagService.createTags(createTagsRequest.getNames());
        List<TagResponse> createdTagResponses =  savedTags.stream().map(tagMapper::tagToTagResponse).toList();
        return new ResponseEntity<>(createdTagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}
