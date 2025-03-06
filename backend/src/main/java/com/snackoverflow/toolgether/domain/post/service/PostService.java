package com.snackoverflow.toolgether.domain.post.service;

import com.snackoverflow.toolgether.domain.post.dto.PostCreateRequest;
import com.snackoverflow.toolgether.domain.post.dto.PostResponse;
import com.snackoverflow.toolgether.domain.post.dto.PostUpdateRequest;
import jakarta.validation.Valid;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);

    PostResponse getPostById(Long postId);

    void deletePost(Long postId);

    PostResponse updatePost(Long postId, @Valid PostUpdateRequest request);
}
