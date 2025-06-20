package com.blog.blogbackend.mappers;

import com.blog.blogbackend.dtos.UserDto;
import com.blog.blogbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
}
