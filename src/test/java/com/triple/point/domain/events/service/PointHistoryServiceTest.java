package com.triple.point.domain.events.service;

import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.common.type.ReviewType;
import com.triple.point.domain.events.dto.PointRequest;
import com.triple.point.domain.events.dto.PointResponse;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.domain.events.entity.User;
import com.triple.point.domain.events.repository.PointHistoryRepository;
import com.triple.point.domain.events.repository.ReviewRepository;
import com.triple.point.domain.events.repository.UserRepository;
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
    private EventsReviewPointService eventsReviewPointService;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    private PointRequest request;

    @BeforeEach
    public void init() {
        request = new PointRequest(
                "REVIEW", "ADD", "313a0658-dc5f-4878-9381-ebb7b2667772", "좋아요!",
                List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f");

        // 테스트 전 프록시 안 서비스 객체 넣어주기
        ReflectionTestUtils.setField(
                eventServiceProxy, "serviceMap", Map.of(EventType.REVIEW, eventsReviewPointService)
        );
    }

    @DisplayName("[proxy]첫 리뷰 보너스 포인트 테스트")
    @Test
    public void first_review_ADD_test() throws Exception {
        // given
        Review firstReview = request.toFirstReview();

        // when
        when(reviewRepository.findByPlaceId(any()))
                .thenReturn(new ArrayList<>());
        when(reviewRepository.findByPlaceIdAndUser(
                request.getPlaceId(), request.getUUID(PointRequest.USER)
                )).thenReturn(Optional.of(firstReview));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getReviewType()).isEqualTo(ReviewType.FIRST);
        assertThat(response.getReviewId()).isEqualTo(request.getReviewId());
        assertThat(response.getIncreasePoint()).isEqualTo(3);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 O, 사진 O) 테스트")
    @Test
    public void not_first_text_photo_ADD_test() throws Exception {
        // given
        Review normalReview = request.toNormalReview();

        // when
        when(reviewRepository.findByPlaceId(any()))
                .thenReturn(List.of(normalReview));
        when(reviewRepository.findByPlaceIdAndUser(
                request.getPlaceId(), request.getUUID(PointRequest.USER)
        )).thenReturn(Optional.of(normalReview));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getReviewType()).isEqualTo(ReviewType.NORMAL);
        assertThat(response.getReviewId()).isEqualTo(request.getReviewId());
        assertThat(response.getIncreasePoint()).isEqualTo(2);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 X, 사진 O) 테스트")
    @Test
    public void not_first_photo_ADD_test() throws Exception {
        // given
        request.setAttachedPhotoIds(new ArrayList<>());
        Review normalReview = request.toNormalReview();

        // when
        when(reviewRepository.findByPlaceId(any()))
                .thenReturn(List.of(normalReview));
        when(reviewRepository.findByPlaceIdAndUser(
                request.getPlaceId(), request.getUUID(PointRequest.USER)
        )).thenReturn(Optional.of(normalReview));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getReviewType()).isEqualTo(ReviewType.NORMAL);
        assertThat(response.getReviewId()).isEqualTo(request.getReviewId());
        assertThat(response.getIncreasePoint()).isEqualTo(1);
    }

    @DisplayName("[proxy]리뷰 기본 포인트(글자수 X, 사진 X) 테스트")
    @Test
    public void not_first_ADD_test() throws Exception {
        // given
        request.setAttachedPhotoIds(new ArrayList<>());
        request.setContent("");
        Review normalReview = request.toNormalReview();

        // when
        when(reviewRepository.findByPlaceId(any()))
                .thenReturn(List.of(normalReview));
        when(reviewRepository.findByPlaceIdAndUser(
                request.getPlaceId(), request.getUUID(PointRequest.USER)
        )).thenReturn(Optional.of(normalReview));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getReviewType()).isEqualTo(ReviewType.NORMAL);
        assertThat(response.getReviewId()).isEqualTo(request.getReviewId());
        assertThat(response.getIncreasePoint()).isEqualTo(0);
    }

    @DisplayName("[proxy] MOD 수정 내역 추가 테스트")
    @Test
    public void MOD_test() throws Exception {
        // given
        User user = request.toUser();
        Review review = request.toNormalReview();
        user.addReview(review);
        request.setAction(ActionType.MOD.name());
        request.setContent("");
        request.setAttachedPhotoIds(new ArrayList<>());
        // content o, photo o => normal 2 point => modify x, x => 0 point & increase -2

        // when
        when(reviewRepository.findById(request.getUUID(PointRequest.REVIEW)))
                .thenReturn(Optional.of(review));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getReviewType()).isEqualTo(ReviewType.NORMAL);
        assertThat(response.getIncreasePoint()).isEqualTo(-2);
    }

    @DisplayName("[proxy] DELETE 삭제 내역 추가 테스트")
    @Test
    public void DELETE_test() throws Exception {
        // given
        User user = request.toUser();
        Review normalReview = request.toNormalReview();
        user.addReview(normalReview);
        request.setAction(ActionType.DELETE.name());

        // when
        when(reviewRepository.findById(request.getUUID(PointRequest.REVIEW)))
                .thenReturn(Optional.of(normalReview));

        PointResponse response = (PointResponse) eventServiceProxy.invoke(request.getType(), request.getAction(), request);

        // then
        assertThat(response.getHistoryUserTotal()).isEqualTo(0);
        assertThat(response.getIncreasePoint()).isEqualTo(-2);
    }
}