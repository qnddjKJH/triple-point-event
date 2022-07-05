package com.triple.point.domain.points.controller;

import com.triple.point.domain.common.dto.EventResponseEntity;
import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.dto.TotalPointResponse;
import com.triple.point.domain.points.service.PointHistoryService;
import com.triple.point.proxy.EventServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@RequestMapping("/points")
@RequiredArgsConstructor
@RestController
public class PointsController {

    private final EventServiceProxy eventServiceProxy;
    private final PointHistoryService pointHistoryService;

    @GetMapping("")
    public EventResponseEntity allHistory() {
        List<PointHistoryResponse> allPointHistories = pointHistoryService.getAllPointHistories();
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("ALL-HISTORY")
                .data(allPointHistories)
                .build();
    }

    @GetMapping("/{userId}")
    public EventResponseEntity getUserHistories(@PathVariable("userId") String userId) {
        List<PointHistoryResponse> userPointHistories = pointHistoryService.getUserPointHistories(userId);
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("USER-HISTORY")
                .data(userPointHistories)
                .build();
    }

    /**
     * 사용자 포인트 총점 조회 API
     *
     * */
    @GetMapping("/{userId}/total")
    public EventResponseEntity userTotalPoint(@PathVariable("userId") String userId) {
        int userTotalPoint = pointHistoryService.userTotalPoint(userId);
        TotalPointResponse response = TotalPointResponse.builder()
                .userId(userId)
                .totalPoint(userTotalPoint)
                .build();
        return EventResponseEntity.successResponse()
                .status(HttpStatus.OK)
                .message("TOTAL")
                .data(response)
                .build();
    }


    @PostMapping("")
    public EventResponseEntity points(@RequestBody PointHistoryRequest request)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        log.info("[POST] :: /posts, request :: {}", request);
        //PointHistoryResponse pointHistoryResponse = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);
        log.info("[POST] :: /posts, response {}", response.toString());

        return EventResponseEntity.successResponse()
                .message(response.getAction().name())
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
