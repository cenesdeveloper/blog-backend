package com.blog.blogbackend.controllers;

import com.blog.blogbackend.dtos.TagResponse;
import com.blog.blogbackend.entities.Tag;
import com.blog.blogbackend.mappers.TagMapper;
import com.blog.blogbackend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
