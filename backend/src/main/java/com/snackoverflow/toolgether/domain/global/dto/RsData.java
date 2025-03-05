package com.snackoverflow.toolgether.domain.global.dto;

import lombok.Getter;

@Getter
public class RsData<T> {
    private final String code;   // 응답 코드 (예: "201-1")
    private final String message;  // 응답 메시지 (예: "게시물이 성공적으로 등록되었습니다.")
    private final T data; // 실제 데이터 (제네릭 타입)

    public RsData(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 성공 여부 확인하는 메서드
    public boolean isSuccess() {
        return code.startsWith("2"); // 2xx 응답이면 성공
    }
}
