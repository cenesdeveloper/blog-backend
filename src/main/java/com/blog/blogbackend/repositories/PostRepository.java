package com.blog.blogbackend.repositories;

import com.blog.blogbackend.domain.PostStatus;
import com.blog.blogbackend.domain.entities.Category;
import com.blog.blogbackend.domain.entities.Post;
import com.blog.blogbackend.domain.entities.Tag;
import com.blog.blogbackend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//@Repository
//public interface PostRepository extends JpaRepository<Post, UUID> {
//    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
//    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
//    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
//    List<Post> findAllByStatus(PostStatus status);
//    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
//}

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    // No filter
    List<Post> findAllByStatus(PostStatus status);

    // Author + status
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);

    List<Post> findDistinctByStatusAndCategory_IdInAndTags_IdIn(PostStatus postStatus, List<UUID> categoryIds, List<UUID> tagIds);

    List<Post> findDistinctByStatusAndCategory_IdIn(PostStatus postStatus, List<UUID> categoryIds);

    List<Post> findDistinctByStatusAndTags_IdIn(PostStatus postStatus, List<UUID> tagIds);
}