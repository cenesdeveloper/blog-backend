package com.blog.blogbackend.mappers;

import com.blog.blogbackend.CreatePostRequest;
import com.blog.blogbackend.dtos.CreatePostRequestDto;
import com.blog.blogbackend.dtos.PostDto;
import com.blog.blogbackend.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);
}
