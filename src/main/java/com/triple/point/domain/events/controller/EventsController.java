package com.triple.point.domain.events.controller;

import com.triple.point.domain.common.dto.EventResponseEntity;
import com.triple.point.domain.events.dto.PointRequest;
import com.triple.point.domain.events.dto.PointResponse;
import com.triple.point.domain.events.dto.TotalReviewPointResponse;
import com.triple.point.domain.events.service.EventsReviewPointService;
import com.triple.point.proxy.EventServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
@RestController
public class EventsController {

    private final EventServiceProxy eventServiceProxy;
    private final EventsReviewPointService eventsReviewPointService;

    /** 전체 이력 조회 */
    @GetMapping("")
    public EventResponseEntity allHistory() {
        List<PointResponse> allPointHistories = eventsReviewPointService.getAllPointHistories();
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("ALL-HISTORY")
                .data(allPointHistories)
                .build();
    }

    /** 사용자 내역 조회 */
    @GetMapping("/{userId}")
    public EventResponseEntity getUserHistories(@PathVariable("userId") String userId) {
        List<PointResponse> userPointHistories = eventsReviewPointService.getUserPointHistories(userId);
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("USER-HISTORY")
                .data(userPointHistories)
                .build();
    }

    /** 사용자 총점 조회 */
    @GetMapping("/{userId}/total")
    public EventResponseEntity userTotalPoint(@PathVariable("userId") String userId) {
        TotalReviewPointResponse response = eventsReviewPointService.userTotalPoint(userId);

        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("TOTAL")
                .data(response)
                .build();
    }

    /** 이벤트 도메인 API */
    @PostMapping("")
    public EventResponseEntity points(@RequestBody PointRequest request)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);
        log.info("response :: {}", response);
        return EventResponseEntity.successResponse()
                .message(response.getAction().name())
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
