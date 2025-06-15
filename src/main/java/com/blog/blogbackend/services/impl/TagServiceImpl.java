package com.blog.blogbackend.services.impl;

import com.blog.blogbackend.entities.Tag;
import com.blog.blogbackend.repositories.TagRepository;
import com.blog.blogbackend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
}
