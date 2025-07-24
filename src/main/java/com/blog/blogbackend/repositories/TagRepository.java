package com.blog.blogbackend.repositories;

import com.blog.blogbackend.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository  extends JpaRepository<Tag, UUID> {

}
