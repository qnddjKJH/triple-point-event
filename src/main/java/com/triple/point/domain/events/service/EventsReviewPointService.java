package com.triple.point.domain.events.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.exception.CustomException;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.ExceptionType;
import com.triple.point.domain.events.dto.EventReviewPointRequest;
import com.triple.point.domain.events.dto.EventReviewPointResponse;
import com.triple.point.domain.events.dto.TotalReviewPointResponse;
import com.triple.point.domain.events.entity.EventsReviewPoint;
import com.triple.point.domain.events.entity.UserPoint;
import com.triple.point.domain.events.repository.EventsReviewPointRepository;
import com.triple.point.domain.events.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventsReviewPointService implements EventsService {

    private final EventsReviewPointRepository eventsReviewPointRepository;

    private final UserPointRepository userPointRepository;

    public List<EventReviewPointResponse> getAllPointHistories() {
        return eventsReviewPointRepository.findAll().stream()
                .map(EventReviewPointResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 포인트 총합 반환
     *
     * @param userId : 포인트 총합 대상 사용자 UUID
     * @return int : 사용자 포인트 총합
     */
    public TotalReviewPointResponse userTotalPoint(String userId) {
        List<EventsReviewPoint> userReviewPoints = eventsReviewPointRepository.findByUserId(userId);
        int totalPoint = userReviewPoints.stream()
                .mapToInt(EventsReviewPoint::getIncreasePoint)
                .sum();
        return TotalReviewPointResponse.builder()
                .userId(userId)
                .totalPoint(totalPoint)
                .reviewCount(userReviewPoints.size())
                .build();
    }

    /** 사용자 포인트 내역 리스트 반환 */
    public List<EventReviewPointResponse> getUserPointHistories(String userId) {
        return eventsReviewPointRepository.findByUserId(userId).stream()
                .map(EventReviewPointResponse::new)
                .collect(Collectors.toList());
    }

    /*
    * 비고
    *  - 사용자는 장소당 하나의 리뷰만 작성 가능
    */
    @Override
    @Transactional
    public EventResponse addEvent(EventRequest eventRequest) {
        EventReviewPointRequest request = (EventReviewPointRequest) eventRequest;
        // 특정 장소의 첫 리뷰 판단
        boolean checkFirstReview = eventsReviewPointRepository.findByPlaceId(request.getPlaceId()).isEmpty();

        // 장소에 해당 유저의 리뷰 내역이 있는지 조회
        // ADD 만 검색 (MOD 와 DELETE 내역들은 ADD 내역이 없으면 성립 불가)
        EventsReviewPoint eventsReviewPoint = eventsReviewPointRepository
                .findByUserIdAndPlaceIdAndAction(
                        request.getUserId(),
                        request.getPlaceId(),
                        request.getAction())
                .orElseGet(request::toEntity);

        UUID userId = UUID.fromString(request.getUserId());
        UserPoint userPoint = userPointRepository.findById(userId)
                .orElseGet(() -> new UserPoint(userId));

        // 요청 유저의 장소 리뷰가 없으면 신규 포인트 내역 생성 및 초기화
        if(eventsReviewPoint.getId() == null || userPoint.checkPlaceReview(request.getPlaceId())) {
            // 특정 장소 첫 리뷰 보너스 포인트
            if(checkFirstReview) {
                eventsReviewPoint.bonusPoint();
                userPoint.addPoint();
            }
            // 지급되는 포인트 및 현 시점 포인트 계산
            eventsReviewPoint.calculationPoint();
            eventsReviewPointRepository.save(eventsReviewPoint);
        } else {
            throw new CustomException(ExceptionType.ALREADY_EXIST_RESOURCE, new EventReviewPointResponse(eventsReviewPoint));
        }

        return new EventReviewPointResponse(eventsReviewPoint);
    }

    @Override
    @Transactional
    public EventResponse modifyEvent(EventRequest eventRequest) {
        EventReviewPointRequest request = (EventReviewPointRequest) eventRequest;

        // 수정 대상 = 사용자가 쓴 리뷰
        EventsReviewPoint recentHistory = eventsReviewPointRepository
                .findTopByReviewIdOrderByCreatedAtDesc(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Not Found History"));

        // 최근 이력이 삭제된 경우 수정할 수 없음 삭제는 ADD, MOD 이력만으로 판단.
        isDeletedHistory(recentHistory);
        
        EventsReviewPoint modifyHistory = request.toEntity();
        modifyHistory.modifyPoint(recentHistory.isFirstReview(), request, recentHistory.getCurrentPoint());

        eventsReviewPointRepository.save(modifyHistory);
        return new EventReviewPointResponse(modifyHistory);
    }

    @Override
    @Transactional
    public EventResponse deleteEvent(EventRequest eventRequest) {
        EventReviewPointRequest request = (EventReviewPointRequest) eventRequest;

        // 최근 이력이 없음 == 삭제할 리뷰가 없음으로 판단 = 에러
        EventsReviewPoint targetHistory = eventsReviewPointRepository.findTopByReviewIdOrderByCreatedAtDesc(request.getReviewId())
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_REVIEW));
        
        // 최근 이력이 삭제된 경우 삭제할 수 없음 삭제는 ADD, MOD 이력만으로 판단.
        isDeletedHistory(targetHistory);
        
        EventsReviewPoint eventsReviewPoint = request.toEntity();
        eventsReviewPoint.deletePoint(targetHistory.getCurrentPoint(), targetHistory.isFirstReview());

        eventsReviewPointRepository.save(eventsReviewPoint);
        return new EventReviewPointResponse(eventsReviewPoint);
    }

    private void isDeletedHistory(EventsReviewPoint targetHistory) {
        if (targetHistory.getAction() == ActionType.DELETE) {
            throw new CustomException(ExceptionType.BAD_REQUEST_ACTION);
        }
    }
}
