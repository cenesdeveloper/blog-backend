package com.blog.blogbackend.services;

import com.blog.blogbackend.domain.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTags();
}
