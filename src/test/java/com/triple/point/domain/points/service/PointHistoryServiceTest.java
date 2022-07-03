package com.triple.point.domain.points.service;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.entity.ActionType;
import com.triple.point.domain.points.entity.PointHistory;
import com.triple.point.domain.points.repository.PointHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

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
    }

    @DisplayName("첫 리뷰 보너스 포인트 테스트")
    @Test
    public void first_review_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = request.toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(new ArrayList<>());
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), ActionType.valueOf(request.getAction())
                )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(3);
        assertThat(response.getCurrentPoint()).isEqualTo(3);
    }

    @DisplayName("리뷰 기본 포인트(글자수 O, 사진 O) 테스트")
    @Test
    public void not_first_ADD_test() throws Exception {
        // given
        PointHistory pointHistory = request.toEntity();

        // when
        when(pointHistoryRepository.findByPlaceId(any()))
                .thenReturn(List.of(pointHistory)); // 비어있지 않으므로 첫 리뷰로 판단하지 않음
        when(pointHistoryRepository.findByUserIdAndPlaceIdAndAction(
                request.getUserId(), request.getPlaceId(), ActionType.valueOf(request.getAction())
        )).thenReturn(Optional.of(pointHistory));
        when(pointHistoryRepository.save(pointHistory))
                .thenReturn(pointHistory);

        PointHistoryResponse response = pointHistoryService.pointsServiceManager(request);

        // then
        assertThat(response.getIncreasePoint()).isEqualTo(2);
        assertThat(response.getCurrentPoint()).isEqualTo(2);
    }

}