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

    @GetMapping("")
    public EventResponseEntity allHistory() {
        List<PointResponse> allPointHistories = eventsReviewPointService.getAllPointHistories();
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("ALL-HISTORY")
                .data(allPointHistories)
                .build();
    }

    @GetMapping("/{userId}")
    public EventResponseEntity getUserHistories(@PathVariable("userId") String userId) {
        List<PointResponse> userPointHistories = eventsReviewPointService.getUserPointHistories(userId);
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("USER-HISTORY")
                .data(userPointHistories)
                .build();
    }

    @GetMapping("/{userId}/total")
    public EventResponseEntity userTotalPoint(@PathVariable("userId") String userId) {
        TotalReviewPointResponse response = eventsReviewPointService.userTotalPoint(userId);

        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("TOTAL")
                .data(response)
                .build();
    }


    @PostMapping("")
    public EventResponseEntity points(@RequestBody PointRequest request)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        return EventResponseEntity.successResponse()
                .message(response.getAction().name())
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
