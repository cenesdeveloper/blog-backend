package com.blog.blogbackend.services;

import com.blog.blogbackend.domain.dtos.CategoryDto;
import com.blog.blogbackend.domain.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories();
}
