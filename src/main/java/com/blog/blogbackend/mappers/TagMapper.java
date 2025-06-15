package com.blog.blogbackend.mappers;

import com.blog.blogbackend.PostStatus;
import com.blog.blogbackend.dtos.TagResponse;
import com.blog.blogbackend.entities.Post;
import com.blog.blogbackend.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse tagToTagResponse(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(List<Post> posts) {
        if(posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
