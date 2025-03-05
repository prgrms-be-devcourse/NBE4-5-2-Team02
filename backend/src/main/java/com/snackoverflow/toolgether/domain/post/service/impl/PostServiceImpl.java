package com.snackoverflow.toolgether.domain.post.service.impl;

import com.snackoverflow.toolgether.domain.global.exception.BadRequestException;
import com.snackoverflow.toolgether.domain.global.exception.NotFoundException;
import com.snackoverflow.toolgether.domain.post.dto.PostCreateRequest;
import com.snackoverflow.toolgether.domain.post.dto.PostResponse;
import com.snackoverflow.toolgether.domain.post.entity.Post;
import com.snackoverflow.toolgether.domain.User;
import com.snackoverflow.toolgether.domain.post.repository.PostRepository;
import com.snackoverflow.toolgether.domain.post.service.PostService;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
//    private final UserRepository userRepository;

    @Transactional
    @Override
    public PostResponse createPost(PostCreateRequest request) {
        // 임시로 user_id=1 사용 (실제 로직에서는 인증된 사용자 정보 가져오기)
        /* User user = userRepository.findById(1L)
                .orElseThrow(() -> new new NotFoundException("404-1", "사용자를 찾을 수 없습니다.")); */

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new BadRequestException("400-1", "제목은 필수 입력값입니다.");
        }

        // mock data
        User user = User.builder()
                .id(1L) // ID 설정
                .username("tempUser")
                .nickname("tempNickname")
                .phoneNumber("010-1234-5678")
                .latitude(37.5665)
                .longitude(126.9780)
                .score(30)
                .credit(0)
                .build();

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .priceType(request.getPriceType())
                .price(request.getPrice())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .viewCount(0)
                .build();

        postRepository.save(post);
        return new PostResponse(post);
    }

    @Transactional
    @Override
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("404-1", "해당 게시물을 찾을 수 없습니다."));

        return new PostResponse(post);
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("404-1", "해당 게시물을 찾을 수 없습니다."));
        postRepository.delete(post);
    }
}
