package com.blog.blogbackend.services.impl;

import com.blog.blogbackend.domain.entities.Category;
import com.blog.blogbackend.repositories.CategoryRepository;
import com.blog.blogbackend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }
}
