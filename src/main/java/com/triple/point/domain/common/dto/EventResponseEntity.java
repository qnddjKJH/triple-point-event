package com.triple.point.domain.common.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EventResponseEntity {
    private final String message;
    private final HttpStatus status;
    private final Object data;

    @Builder(builderMethodName = "successResponse")
    public EventResponseEntity(String message, HttpStatus status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
