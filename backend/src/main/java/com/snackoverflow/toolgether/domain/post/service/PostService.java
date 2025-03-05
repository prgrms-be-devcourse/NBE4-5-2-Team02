package com.snackoverflow.toolgether.domain.post.service;

import com.snackoverflow.toolgether.domain.post.dto.PostCreateRequest;
import com.snackoverflow.toolgether.domain.post.dto.PostResponse;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);
    PostResponse getPostById(Long postId);
    void deletePost(Long postId);
}
