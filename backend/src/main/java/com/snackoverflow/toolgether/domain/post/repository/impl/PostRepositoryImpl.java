package com.snackoverflow.toolgether.domain.post.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snackoverflow.toolgether.domain.post.dto.PostResponse;
import com.snackoverflow.toolgether.domain.post.dto.PostSearchRequest;
import com.snackoverflow.toolgether.domain.post.entity.Post;
import com.snackoverflow.toolgether.domain.post.entity.QPost;
import com.snackoverflow.toolgether.domain.post.repository.PostRepositoryCustom;
import com.snackoverflow.toolgether.domain.postavailability.entity.PostAvailability;
import com.snackoverflow.toolgether.domain.postavailability.entity.QPostAvailability;
import com.snackoverflow.toolgether.domain.postimage.entity.QPostImage;
import com.snackoverflow.toolgether.domain.user.entity.User;
import com.snackoverflow.toolgether.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private final QPost post = QPost.post;
    private final QPostImage postImage = QPostImage.postImage;
    private final QPostAvailability postAvailability = QPostAvailability.postAvailability;

    @Override
    public Page<PostResponse> searchPosts(PostSearchRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        log.info("검색 키워드: {}", request.getKeyword());

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            BooleanBuilder keywordBuilder = new BooleanBuilder();
            keywordBuilder.or(post.title.containsIgnoreCase(request.getKeyword()));
            keywordBuilder.or(post.content.containsIgnoreCase(request.getKeyword()));
            builder.and(keywordBuilder);
        }

        if (request.getCategory() != null) {
            builder.and(post.category.eq(request.getCategory()));
        }

        if (request.getPriceType() != null) {
            builder.and(post.priceType.eq(request.getPriceType()));
        }

        if (request.getMinPrice() != null) {
            builder.and(post.price.goe(request.getMinPrice()));
        }

        if (request.getMaxPrice() != null) {
            builder.and(post.price.loe(request.getMaxPrice()));
        }

        if (request.getLatitude() != null && request.getLongitude() != null && request.getDistance() != null) {
            double latitude = request.getLatitude();
            double longitude = request.getLongitude();
            double distanceInKm = request.getDistance();

            log.info("lat = {}, lon = {}, distance = {}", latitude, longitude, distanceInKm);

            checkDistance(latitude, longitude, builder, distanceInKm);
        } else {
            // 사용자 ID를 기반으로 데이터베이스에서 사용자 정보를 조회
            User user = userRepository.findById(Long.valueOf(request.getUserId()))
                    .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

            // 사용자 정보에서 위도와 경도를 가져옴
            double latitude = user.getLatitude();
            double longitude = user.getLongitude();
            double distanceInKm = request.getDistance();

            log.info("Request에서 lat/lon이 없으므로 사용자 데이터베이스에서 가져옵니다: lat = {}, lon = {}", latitude, longitude);

            checkDistance(latitude, longitude, builder, distanceInKm);
        }

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.postImages, postImage).fetchJoin()
                .leftJoin(post.postAvailabilities, postAvailability).fetchJoin()
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<PostResponse> postResponses = results.stream()
                .map(p -> {
                    Set<String> imageUrls = p.getPostImages() != null
                            ? p.getPostImages().stream()
                            .map(img -> {
                                log.info("게시물 ID: {}, 이미지 ID: {}, URL: {}", p.getId(), img.getId(), img.getImageUrl());
                                return img.getImageUrl();
                            })
                            .collect(Collectors.toSet())
                            : new HashSet<>();

                    Set<PostAvailability> availabilities = p.getPostAvailabilities() != null
                            ? new HashSet<>(p.getPostAvailabilities())
                            : new HashSet<>();

                    log.info("PostResponse 생성 전 - 게시물 ID: {}, 이미지 개수: {}, 스케줄 개수: {}",
                            p.getId(), imageUrls.size(), availabilities.size());

                    return new PostResponse(p, imageUrls, availabilities);
                })
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(postResponses, pageable, () -> results.size());
    }

    private void checkDistance(double latitude, double longitude, BooleanBuilder builder, double distanceInKm) {
        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                latitude, post.latitude, post.longitude, longitude);

        builder.and(distanceExpression.loe(distanceInKm));
    }
}
