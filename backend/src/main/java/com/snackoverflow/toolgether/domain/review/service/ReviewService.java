package com.snackoverflow.toolgether.domain.review.service;

import com.snackoverflow.toolgether.domain.reservation.dto.ReservationResponse;
import com.snackoverflow.toolgether.domain.reservation.entity.Reservation;
import com.snackoverflow.toolgether.domain.review.dto.request.ReviewRequest;
import com.snackoverflow.toolgether.domain.review.entity.Review;
import com.snackoverflow.toolgether.domain.review.repository.ReviewRepository;
import com.snackoverflow.toolgether.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    //해당 예약에 유저가 작성한 리뷰 조회
    public Optional<Review> findByUserIdAndReservationId(Long userId, Long reservationId) {
        return reviewRepository.findByReviewerIdAndReservationId(userId, reservationId);
    }

    public void create(ReviewRequest reviewRequest, Reservation reservation, User user) {
        User reviewee = reservation.getRenter() == user ? reservation.getOwner() : reservation.getRenter();
        Review review = Review.builder()
                .reviewer(user)
                .reviewee(reviewee)
                .reservation(reservation)
                .productScore(reviewRequest.getProductScore())
                .timeScore(reviewRequest.getTimeScore())
                .kindnessScore(reviewRequest.getKindnessScore())
                .build();
        reviewRepository.save(review);
    }
}
