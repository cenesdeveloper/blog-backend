package com.blog.blogbackend.services;

import com.blog.blogbackend.domain.dtos.CategoryDto;
import com.blog.blogbackend.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
    void deleteCategory(UUID id);
    Category getCategoryById(UUID id);
}
