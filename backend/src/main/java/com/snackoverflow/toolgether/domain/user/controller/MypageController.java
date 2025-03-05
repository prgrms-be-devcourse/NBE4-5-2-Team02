package com.snackoverflow.toolgether.domain.user.controller;

import com.snackoverflow.toolgether.domain.reservation.service.ReservationService;
import com.snackoverflow.toolgether.domain.review.sevice.ReviewService;
import com.snackoverflow.toolgether.domain.user.dto.MeInfoResponse;
import com.snackoverflow.toolgether.domain.user.service.UserService;
import com.snackoverflow.toolgether.global.dto.RsData;
import com.snackoverflow.toolgether.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ReservationService reservationService;

    @GetMapping("/me")
    public RsData<MeInfoResponse> me() {
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
}
