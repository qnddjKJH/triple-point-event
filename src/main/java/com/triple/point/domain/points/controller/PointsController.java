package com.triple.point.domain.points.controller;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.dto.TotalPointResponse;
import com.triple.point.domain.points.service.PointHistoryService;
import com.triple.point.proxy.EventServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PointHistoryResponse>> allHistory() {
        List<PointHistoryResponse> allPointHistories = pointHistoryService.getAllPointHistories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allPointHistories);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PointHistoryResponse>> getUserHistories(@PathVariable("userId") String userId) {
        List<PointHistoryResponse> userPointHistories = pointHistoryService.getUserPointHistories(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userPointHistories);
    }

    /**
     * 사용자 포인트 총점 조회 API
     *
     * */
    @GetMapping("/{userId}/total-point")
    public ResponseEntity<TotalPointResponse> userTotalPoint(@PathVariable("userId") String userId) {
        int userTotalPoint = pointHistoryService.userTotalPoint(userId);
        TotalPointResponse response = TotalPointResponse.builder()
                .userId(userId)
                .totalPoint(userTotalPoint)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("")
    public ResponseEntity<PointHistoryResponse> savePointHistory(@RequestBody PointHistoryRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        log.info("[POST] :: /posts, request :: {}", request);
        //PointHistoryResponse pointHistoryResponse = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse invoke = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);
        log.info("[PROXY] :: response {}", invoke.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(invoke);
    }
}
