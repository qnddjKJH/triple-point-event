package com.triple.point.domain.common.exception;

import com.triple.point.domain.common.type.ExceptionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomException extends RuntimeException {
    private int code;
    private String message;
    private Object data;

    public CustomException(ExceptionType exceptionType) {
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public CustomException(ExceptionType exceptionType, Object data) {
        this(exceptionType);
        this.data = data;
    }
}
