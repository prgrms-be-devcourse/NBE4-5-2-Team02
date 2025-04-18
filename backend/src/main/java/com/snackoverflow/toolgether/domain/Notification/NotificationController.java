package com.snackoverflow.toolgether.domain.Notification;

import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	// 사용자 ID로 읽지 않은 알림 목록 조회
	@GetMapping("/unread/{userId}")
	public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@PathVariable Long userId) {
		List<NotificationDto> notifications = notificationService.getUnreadNotifications(userId);
		return ResponseEntity.ok(notifications);
	}

	// 알림 읽음 처리
	@PutMapping("/{notificationId}")
	public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId) {
		try {
			notificationService.markNotificationAsRead(notificationId);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// ... (기존 코드)
}