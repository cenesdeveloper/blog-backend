package com.blog.blogbackend.mappers;

import com.blog.blogbackend.domain.CreatePostRequest;
import com.blog.blogbackend.domain.UpdatePostRequest;
import com.blog.blogbackend.domain.dtos.CreatePostRequestDto;
import com.blog.blogbackend.domain.dtos.PostDto;
import com.blog.blogbackend.domain.dtos.UpdatePostRequestDto;
import com.blog.blogbackend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toPostDto(Post post);

    @Mapping(target = "status", source = "status")
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto  createPostRequestDto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto  updatePostRequestDto);
}
