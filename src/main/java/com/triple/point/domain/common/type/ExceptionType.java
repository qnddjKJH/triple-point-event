package com.triple.point.domain.common.type;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    ALREADY_EXIST_RESOURCE(HttpStatus.CONFLICT.value(), "이미 존재하는 리소스입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "사용자를 찾지 못하였습니다."),
    NOT_FOUND_PLACE(HttpStatus.NOT_FOUND.value(), "장소를 찾지 못하였습니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND.value(), "리뷰를 찾지 못하였습니다."),
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND.value(), "리소스를 찾지 못하였습니다."),
    BAD_REQUEST_TYPE(HttpStatus.BAD_REQUEST.value(), "잘못된 Type 요청입니다."),
    BAD_REQUEST_ACTION(HttpStatus.BAD_REQUEST.value(), "잘못된 Action 요청입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러"),
    ;

    private final int code;
    private final String message;
    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
