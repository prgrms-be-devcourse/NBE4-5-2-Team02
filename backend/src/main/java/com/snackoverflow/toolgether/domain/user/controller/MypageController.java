package com.snackoverflow.toolgether.domain.user.controller;

import com.snackoverflow.toolgether.domain.postimage.entity.PostImage;
import com.snackoverflow.toolgether.domain.postimage.service.PostImageService;
import com.snackoverflow.toolgether.domain.reservation.entity.Reservation;
import com.snackoverflow.toolgether.domain.reservation.service.ReservationService;
import com.snackoverflow.toolgether.domain.review.service.ReviewService;
import com.snackoverflow.toolgether.domain.user.dto.MeInfoResponse;
import com.snackoverflow.toolgether.domain.user.dto.MyReservationInfoResponse;
import com.snackoverflow.toolgether.domain.user.dto.request.PatchMyInfoRequest;
import com.snackoverflow.toolgether.domain.user.entity.User;
import com.snackoverflow.toolgether.domain.user.service.UserService;
import com.snackoverflow.toolgether.global.dto.RsData;
import com.snackoverflow.toolgether.global.filter.CustomUserDetails;
import com.snackoverflow.toolgether.global.filter.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    //내 정보 조회
    @GetMapping("/me")
    public RsData<MeInfoResponse> getMyInfo(
            @Login CustomUserDetails customUserDetails
    ) {
        String username = customUserDetails.getUsername();
        User user = userService.findByUsername(username);
        Long userId = user.getId();
        MeInfoResponse meInfoResponse = userService.getMeInfo(userId);

        return new RsData<>(
                "200-1",
                "내 정보 조회 성공",
                meInfoResponse
        );
    }

    //예약 조회
    @Transactional(readOnly = true)
    @GetMapping("/reservations")
    public RsData<Map<String, List<MyReservationInfoResponse>>> getMyReservations(
            @Login CustomUserDetails customUserDetails
    ) {
        String username = customUserDetails.getUsername();
        User user = userService.findByUsername(username);
        Long userId = user.getId();

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

    //이미지 제외한 수정 가능한 내 정보 수정
    @PatchMapping("/me")
    public RsData<Void> PostMyInfo(
            @Login CustomUserDetails customUserDetails,
            @RequestBody @Validated PatchMyInfoRequest request
    ) {
        String username = customUserDetails.getUsername();
        User user = userService.findByUsername(username);
        userService.checkMyInfoDuplicates(request);
        userService.updateMyInfo(user, request);

        return new RsData<>(
                "200-1",
                "내 정보 수정 성공"
        );
    }

    //회원 탈퇴

}
