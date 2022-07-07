package com.triple.point.domain.common.exception;

import com.triple.point.domain.common.dto.ExceptionResponseEntity;
import com.triple.point.domain.common.type.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.triple.point.domain.events.controller")
public class EventsExceptionAdviceController {

    @ExceptionHandler(NullPointerException.class)
    public ExceptionResponseEntity<Object> nullPointer(NullPointerException e) {
        log.info(e.getMessage());
        return ExceptionResponseEntity.failResponse()
                .message(ExceptionType.SERVER_ERROR.getMessage())
                .status(ExceptionType.SERVER_ERROR.getCode())
                .data(e.getStackTrace())
                .build();
    }

    @ExceptionHandler(CustomException.class)
    public ExceptionResponseEntity<Object> customException(CustomException e) {
        log.info(e.getMessage());
        return ExceptionResponseEntity.failResponse()
                .message(e.getMessage())
                .status(e.getCode())
                .data(e.getData())
                .build();
    }
}
