package com.blog.blogbackend.mappers;

import com.blog.blogbackend.domain.PostStatus;
import com.blog.blogbackend.domain.dtos.CategoryDto;
import com.blog.blogbackend.domain.dtos.CreateCategoryRequest;
import com.blog.blogbackend.domain.entities.Category;
import com.blog.blogbackend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source="posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if(null==posts) return 0;
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }

    Category toEntity(CreateCategoryRequest createCategoryRequest);
}
