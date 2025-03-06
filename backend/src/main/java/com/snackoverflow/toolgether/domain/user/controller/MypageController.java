package com.snackoverflow.toolgether.domain.user.controller;

import com.snackoverflow.toolgether.domain.postimage.entity.PostImage;
import com.snackoverflow.toolgether.domain.postimage.service.PostImageService;
import com.snackoverflow.toolgether.domain.reservation.entity.Reservation;
import com.snackoverflow.toolgether.domain.reservation.service.ReservationService;
import com.snackoverflow.toolgether.domain.review.service.ReviewService;
import com.snackoverflow.toolgether.domain.user.dto.MeInfoResponse;
import com.snackoverflow.toolgether.domain.user.dto.MyReservationInfoResponse;
import com.snackoverflow.toolgether.domain.user.service.UserService;
import com.snackoverflow.toolgether.global.dto.RsData;
import com.snackoverflow.toolgether.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ReservationService reservationService;
    private final PostImageService postImageService;

    @GetMapping("/me")
    public RsData<MeInfoResponse> getMyInfo() {
        // TODO: 현재 로그인한 유저의 정보를 가져오는 로직을 추가해야 합니다.
        // 그 전까지는 baseInitData에서 생성한 첫번째 유저를 가져옵니다.
        if (false) {
            throw new ServiceException("401-1", "인증이 필요한 요청입니다");
        }
        long id = 1L;
        MeInfoResponse meInfoResponse = userService.getMeInfo(id);

        return new RsData<>(
                "200-1",
                "내 정보 조회 성공",
                meInfoResponse
        );
    }

    @GetMapping("/reservations")
    public RsData<Map<String, List<MyReservationInfoResponse>>> getMyReservations() {
        // TODO: 현재 로그인한 유저의 예약 정보를 가져오는 로직을 추가해야 합니다.
        // 그 전까지는 baseInitData에서 생성한 첫번째 유저를 가져옵니다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = ((com.snackoverflow.toolgether.domain.user.entity.User) authentication.getPrincipal()).getId(); // UserDetails 인터페이스 구현 필요

        if (false) {
            throw new ServiceException("401-1", "인증이 필요한 요청입니다");
        } else if (false) {
            throw new ServiceException("404-1", "해당 유저를 찾을 수 없습니다");
        } else if (false) {
            throw new ServiceException("403-1", "권한이 없는 요청입니다");
        }
        long userId = 2L;

        List<Reservation> rentals = reservationService.getRentalReservations(userId);
        List<Reservation> borrows = reservationService.getBorrowReservations(userId);

        List<MyReservationInfoResponse> rentalResponses = rentals.stream()
                .map(reservation -> {
                    String imageUrl = null;
                    List<PostImage> images = postImageService.getPostImagesByPostId(reservation.getPost().getId());  //PostImageService 호출
                    if (images != null && !images.isEmpty()) {
                        imageUrl = images.get(0).getPostImage();
                    }
                    boolean isReviewed = reviewService.findByUserIdAndReservationId(userId, reservation.getId()).isPresent();

                    return MyReservationInfoResponse.from(reservation, imageUrl, isReviewed);
                })
                .collect(Collectors.toList());

        List<MyReservationInfoResponse> borrowResponses = borrows.stream()
                .map(reservation -> {
                    String imageUrl = null;
                    List<PostImage> images = postImageService.getPostImagesByPostId(reservation.getPost().getId());  //PostImageService 호출
                    if (images != null && !images.isEmpty()) {
                        imageUrl = images.get(0).getPostImage();
                    }
                    boolean isReviewed = reviewService.findByUserIdAndReservationId(userId, reservation.getId()).isPresent();
                    return MyReservationInfoResponse.from(reservation, imageUrl, isReviewed);
                })
                .collect(Collectors.toList());

        Map<String, List<MyReservationInfoResponse>> data = new HashMap<>();
        data.put("rentals", rentalResponses);
        data.put("borrows", borrowResponses);

        return new RsData<>(
                "200-1",
                "마이페이지 예약 정보 조회 성공",
                data
        );
    }
}
