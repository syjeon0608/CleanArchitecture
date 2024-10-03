package com.hhplus.clean.architecture.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionCode implements ErrorCode{

    // NOT_FOUND
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "신청 가능한 강의가 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    //수강 신청 관련 예외
    DUPLICATE_ENROLLMENT(HttpStatus.BAD_REQUEST, "이미 수강신청 완료하였습니다."),
    LECTURE_FULL(HttpStatus.BAD_REQUEST, "수강인원이 가득 찼습니다."),
    REGISTRATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "수강신청 완료된 강의가 없습니다."),


    // 기타 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
