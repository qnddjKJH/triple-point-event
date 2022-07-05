package com.triple.point.domain.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ExceptionResponseEntity<T> {
    private final String message;
    private final int status;
    private final T data;

    @Builder(builderMethodName = "failResponse")
    public ExceptionResponseEntity(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
