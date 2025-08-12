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
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.domain.dtos.AuthorDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    AuthorDto toAuthorDto(User user);

    @Mapping(target = "author.name", source = "author.name")
    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "author.email", source = "author.email")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toPostDto(Post post);

    @Mapping(target = "status", source = "status")
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto  createPostRequestDto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto  updatePostRequestDto);
}
