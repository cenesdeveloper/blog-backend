package com.blog.blogbackend.services.impl;

import com.blog.blogbackend.domain.CreatePostRequest;
import com.blog.blogbackend.domain.PostStatus;
import com.blog.blogbackend.domain.UpdatePostRequest;
import com.blog.blogbackend.domain.entities.Category;
import com.blog.blogbackend.domain.entities.Post;
import com.blog.blogbackend.domain.entities.Tag;
import com.blog.blogbackend.domain.entities.User;
import com.blog.blogbackend.repositories.PostRepository;
import com.blog.blogbackend.services.CategoryService;
import com.blog.blogbackend.services.PostService;
import com.blog.blogbackend.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id: " + id));
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
//        if(categoryId != null && tagId != null){
//            Category category = categoryService.getCategoryById(categoryId);
//            Tag tag = tagService.getTagById(tagId);
//            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
//        }
//        if(categoryId != null){
//            Category category = categoryService.getCategoryById(categoryId);
//            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
//        }
//        if(tagId != null){
//            Tag tag = tagService.getTagById(tagId);
//            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
//        }
//        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
//
//    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + id));
        // Setting new values
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        // Updating Categories
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        // avoiding unnecessary writes/updates
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        // Updating Tags
        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        // avoiding unnecessary writes/updates
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)){
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null || content.isEmpty()){
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPublishedPosts(List<UUID> categoryIds, List<UUID> tagIds) {
        // same filtering you already had in getAllPosts, but explicit name
        boolean hasCats = categoryIds != null && !categoryIds.isEmpty();
        boolean hasTags = tagIds != null && !tagIds.isEmpty();

        if (hasCats && hasTags) {
            return postRepository.findDistinctByStatusAndCategory_IdInAndTags_IdIn(
                    PostStatus.PUBLISHED, categoryIds, tagIds
            );
        }
        if (hasCats) {
            return postRepository.findDistinctByStatusAndCategory_IdIn(PostStatus.PUBLISHED, categoryIds);
        }
        if (hasTags) {
            return postRepository.findDistinctByStatusAndTags_IdIn(PostStatus.PUBLISHED, tagIds);
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    // Optional: keep old name delegating to new one
    @Deprecated
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(List<UUID> categoryIds, List<UUID> tagIds) {
        return getPublishedPosts(categoryIds, tagIds);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostVisibleTo(UUID id, UUID requesterId) {
        Post post = getPost(id); // throws if not found
        if (post.getStatus() == PostStatus.PUBLISHED) return post;

        // Draft: only owner (ADMIN support can be added later)
        if (requesterId != null && requesterId.equals(post.getAuthor().getId())) {
            return post;
        }
        // Hide existence of drafts to others
        throw new jakarta.persistence.EntityNotFoundException("Post does not exist with id: " + id);
    }

    @Override
    @Transactional
    public Post updatePostAuthorized(UUID id, UpdatePostRequest updatePostRequest, UUID requesterId) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + id));
        if (!existing.getAuthor().getId().equals(requesterId)) {
            throw new org.springframework.security.access.AccessDeniedException("Not allowed to modify this post");
        }
        // reuse your existing update logic
        existing.setTitle(updatePostRequest.getTitle());
        existing.setContent(updatePostRequest.getContent());
        existing.setStatus(updatePostRequest.getStatus());
        existing.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID newCatId = updatePostRequest.getCategoryId();
        if (!existing.getCategory().getId().equals(newCatId)) {
            Category newCategory = categoryService.getCategoryById(newCatId);
            existing.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existing.getTags().stream().map(Tag::getId).collect(java.util.stream.Collectors.toSet());
        Set<UUID> reqTagIds = updatePostRequest.getTagIds();
        if (!existingTagIds.equals(reqTagIds)) {
            List<Tag> newTags = tagService.getTagByIds(reqTagIds);
            existing.setTags(new java.util.HashSet<>(newTags));
        }

        return postRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePostAuthorized(UUID id, UUID requesterId) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + id));
        if (!existing.getAuthor().getId().equals(requesterId)) {
            throw new org.springframework.security.access.AccessDeniedException("Not allowed to delete this post");
        }
        postRepository.delete(existing);
    }

}
