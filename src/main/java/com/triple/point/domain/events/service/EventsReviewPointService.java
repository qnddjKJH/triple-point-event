package com.triple.point.domain.events.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.exception.CustomException;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.ExceptionType;
import com.triple.point.domain.events.dto.*;
import com.triple.point.domain.events.entity.PointHistory;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.domain.events.entity.User;
import com.triple.point.domain.events.repository.PointHistoryRepository;
import com.triple.point.domain.events.repository.ReviewRepository;
import com.triple.point.domain.events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventsReviewPointService implements EventsService {

    private final PointHistoryRepository pointHistoryRepository;

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<PointResponse> getAllPointHistories() {
        return pointHistoryRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PointResponse::new)
                .collect(Collectors.toList());
    }

    public TotalReviewPointResponse userTotalPoint(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_USER));

        return TotalReviewPointResponse.builder()
                .userId(userId)
                .totalPoint(user.getPoint())
                .reviewCount(user.getReviews().size())
                .build();
    }

    /** 사용자 포인트 내역 리스트 반환 */
    public List<PointResponse> getUserPointHistories(String userId) {
        return pointHistoryRepository.findByUserId(userId).stream()
                .map(PointResponse::new)
                .collect(Collectors.toList());
    }

    /*
    * 비고
    *  - 사용자는 장소당 하나의 리뷰만 작성 가능
    */
    @Override
    @Transactional
    public EventResponse addEvent(EventRequest eventRequest) {
        PointRequest request = (PointRequest) eventRequest;
        // 특정 장소 리뷰 확인 (true : firstReview, false : normalReview)
        boolean isReviewInPlace = reviewRepository.findByPlaceId(request.getPlaceId()).isEmpty();

        Review review = reviewRepository
                .findByPlaceIdAndUser(request.getPlaceId(), request.getUUID(PointRequest.USER))
                .orElseGet(() -> isReviewInPlace ? request.toFirstReview() : request.toNormalReview());

        boolean isUserEmpty = Optional.ofNullable(review.getUser()).isEmpty();

        PointHistory history = request.toHistory();
        if (isUserEmpty) {
            // 유저가 비어있다면 새로 만든 리뷰이다.
            User user = userRepository.findById(request.getUUID(PointRequest.USER))
                    .orElseGet(request::toUser);
            user.addReview(review);
            userRepository.save(user);

            history.setIncreasePoint(review.getPoint());
            history.setUserInto(user);
            history.setReviewInfo(review);

            pointHistoryRepository.save(history);
        } else {
            throw new CustomException(ExceptionType.ALREADY_EXIST_RESOURCE, new ReviewResponse(review));
        }

        return new PointResponse(history);
    }

    @Override
    @Transactional
    public EventResponse modifyEvent(EventRequest eventRequest) {
        PointRequest request = (PointRequest) eventRequest;

        Review review = reviewRepository.findById(request.getUUID(PointRequest.REVIEW))
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_REVIEW));

        int increasePoint = review.modifyReview(request.getContent(), request.getAttachedPhotoIds().size());

        PointHistory history = request.toHistory();
        history.setReviewInfo(review);
        history.setUserInto(review.getUser());
        history.setIncreasePoint(increasePoint);
        pointHistoryRepository.save(history);

        return new PointResponse(history);
    }

    @Override
    @Transactional
    public EventResponse deleteEvent(EventRequest eventRequest) {
        PointRequest request = (PointRequest) eventRequest;

        // 삭제할 리뷰가 없음 = 에러
        Review review = reviewRepository.findById(request.getUUID(PointRequest.REVIEW))
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_REVIEW));

        int deletePoint = review.deleteReview();

        PointHistory history = request.toHistory();
        history.setReviewInfo(review);
        history.setUserInto(review.getUser());
        history.setIncreasePoint(deletePoint);

        reviewRepository.delete(review);

        pointHistoryRepository.save(history);
        return new PointResponse(history);
    }
}
