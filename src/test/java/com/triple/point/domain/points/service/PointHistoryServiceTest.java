package com.triple.point.domain.points.service;

import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.entity.PointHistory;
import com.triple.point.domain.points.repository.PointHistoryRepository;
import com.triple.point.proxy.EventServiceProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

    // 기존 사용하던 pointHistoryService.pointsServiceManager 가 사용 제한되고
    // 역할을 EventServiceProxy 가 가져감
    @InjectMocks
    private EventServiceProxy eventServiceProxy;

    @InjectMocks
    private PointHistoryService pointHistoryService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    private PointHistoryRequest request;

    @BeforeEach
    public void init() {
        request = new PointHistoryRequest(
                "REVIEW", "ADD", "313a0658-dc5f-4878-9381-ebb7b2667772", "좋아요!",
                List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f");

        // 테스트 전 프록시 안 서비스 객체 넣어주기
        ReflectionTestUtils.setField(
                eventServiceProxy, "serviceMap", Map.of("REVIEW", pointHistoryService)
        );
    }

    @DisplayName("[proxy]첫 리뷰 보너스 포인트 테스트")
    @Test
    public void first_review_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = request.toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(new ArrayList<>());
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), request.getAction()
                )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

//        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(3);
        assertThat(response.getCurrentPoint()).isEqualTo(3);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 O, 사진 O) 테스트")
    @Test
    public void not_first_text_photo_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = request.toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(List.of(pointHistory)); // 비어있지 않으므로 첫 리뷰로 판단하지 않음
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), request.getAction()
        )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

//        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(2);
        assertThat(response.getCurrentPoint()).isEqualTo(2);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 X, 사진 O) 테스트")
    @Test
    public void not_first_photo_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = new PointHistoryRequest(
                "REVIEW", "ADD", "313a0658-dc5f-4878-9381-ebb7b2667772", "",
                List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f").toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(List.of(pointHistory)); // 비어있지 않으므로 첫 리뷰로 판단하지 않음
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), request.getAction()
        )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

//        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(1);
        assertThat(response.getCurrentPoint()).isEqualTo(1);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 X, 사진 X) 테스트")
    @Test
    public void not_first_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = new PointHistoryRequest(
                "REVIEW", "ADD", "313a0658-dc5f-4878-9381-ebb7b2667772", "",
                List.of(),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f").toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(List.of(pointHistory)); // 비어있지 않으므로 첫 리뷰로 판단하지 않음
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), request.getAction()
        )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

//        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);
        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(0);
        assertThat(response.getCurrentPoint()).isEqualTo(0);
    }

    @DisplayName("[proxy] MOD 수정 내역 추가 테스트")
    @Test
    public void MOD_test() throws Exception {
        // 증가포인트 3, 현재 포인트 3점 짜리 내역이 들었있음 (첫글)
        // 사진 및 글자수 0으로 수정 예상 증감 포인트 -2, 현재 포인트 1 로 MOD 수정 포인트 지급 내역이 들어감

        // given
        PointHistory pointHistory = request.toEntity();
        ReflectionTestUtils.invokeMethod(pointHistory, "bonusPoint");
        ReflectionTestUtils.invokeMethod(pointHistory, "calculationPoint");

        PointHistoryRequest modifyRequest = new PointHistoryRequest(
                "REVIEW", "MOD", "313a0658-dc5f-4878-9381-ebb7b2667772", "1",
                List.of(),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        PointHistory modifyEntity = modifyRequest.toEntity();

        // when
        when(pointHistoryRepository.findTopByReviewIdOrderByCreatedAtDesc(modifyRequest.getReviewId()))
                .thenReturn(Optional.of(pointHistory));
//        when(pointHistoryRepository.save(modifyEntity))
//                .thenReturn(modifyEntity);

        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(modifyRequest.getType(), modifyRequest.getAction(), modifyRequest);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(-1);
        assertThat(response.getCurrentPoint()).isEqualTo(2);
    }

    @DisplayName("[proxy] DELETE 삭제 내역 추가 테스트")
    @Test
    public void DELETE_test() throws Exception {
        // 증가포인트 3, 현재 포인트 3점 짜리 내역이 들었있음 (첫글)
        // 사진 및 글자수 0으로 수정 예상 증감 포인트 -2, 현재 포인트 1 로 MOD 수정 포인트 지급 내역이 들어감

        // given
        PointHistory pointHistory = request.toEntity();
        ReflectionTestUtils.invokeMethod(pointHistory, "bonusPoint");
        ReflectionTestUtils.invokeMethod(pointHistory, "calculationPoint");

        // when
        when(pointHistoryRepository.findTopByReviewIdOrderByCreatedAtDesc(request.getReviewId()))
                .thenReturn(Optional.of(pointHistory));
//        when(pointHistoryRepository.save(modifyEntity))
//                .thenReturn(modifyEntity);

        request.setAction(ActionType.DELETE.name());

        PointHistoryResponse response = (PointHistoryResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(-3);
        assertThat(response.getCurrentPoint()).isEqualTo(0);
    }
}