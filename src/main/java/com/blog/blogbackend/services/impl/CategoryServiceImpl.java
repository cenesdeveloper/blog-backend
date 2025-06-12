package com.blog.blogbackend.services.impl;

import com.blog.blogbackend.entities.Category;
import com.blog.blogbackend.repositories.CategoryRepository;
import com.blog.blogbackend.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category name already exists with name: " + category.getName());

        };
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if (category.get().getPosts().size() > 0) {
                throw new IllegalArgumentException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }
}
