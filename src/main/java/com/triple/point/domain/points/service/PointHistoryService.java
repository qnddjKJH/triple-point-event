package com.triple.point.domain.points.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.exception.CustomException;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.ExceptionType;
import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.entity.PointHistory;
import com.triple.point.domain.points.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PointHistoryService implements EventService {

    private final PointHistoryRepository pointHistoryRepository;

    public List<PointHistoryResponse> getAllPointHistories() {
        return pointHistoryRepository.findAll().stream()
                .map(PointHistoryResponse::new)
                .collect(Collectors.toList());
    }

    /** 사용자 포인트 총합 반환
     *
     * @param userId : 포인트 총합 대상 사용자 UUID
     * @return  int : 사용자 포인트 총합
     * */
    public int userTotalPoint(String userId) {
        return pointHistoryRepository.findByUserId(userId).stream()
                .mapToInt(PointHistory::getIncreasePoint)
                .sum();
    }

    /** 사용자 포인트 내역 리스트 반환 */
    public List<PointHistoryResponse> getUserPointHistories(String userId) {
        return pointHistoryRepository.findByUserId(userId).stream()
                .map(PointHistoryResponse::new)
                .collect(Collectors.toList());
    }

    /*
    * 비고
    *  - 사용자는 장소당 하나의 리뷰만 작성 가능
    */
    @Override
    @Transactional
    public EventResponse addEvent(EventRequest eventRequest) {
        PointHistoryRequest request = (PointHistoryRequest) eventRequest;
        // 특정 장소의 첫 리뷰 판단
        boolean checkFirstReview = pointHistoryRepository.findByPlaceId(request.getPlaceId()).isEmpty();

        // 장소에 해당 유저의 리뷰 내역이 있는지 조회
        // ADD 만 검색 (MOD 와 DELETE 내역들은 ADD 내역이 없으면 성립 불가)
        PointHistory pointHistory = pointHistoryRepository
                .findByUserIdAndPlaceIdAndAction(
                        request.getUserId(),
                        request.getPlaceId(),
                        request.getAction())
                .orElseGet(request::toEntity);

        // 요청 유저의 장소 리뷰가 없으면 신규 포인트 내역 생성 및 초기화
        if(pointHistory.getId() == null) {
            // 특정 장소 첫 리뷰 보너스 포인트
            if(checkFirstReview) {
                pointHistory.bonusPoint();
            }
            // 지급되는 포인트 및 현 시점 포인트 계산
            pointHistory.calculationPoint();
            pointHistoryRepository.save(pointHistory);
        } else {
            throw new CustomException(ExceptionType.ALREADY_EXIST_RESOURCE, new PointHistoryResponse(pointHistory));

        }

        return new PointHistoryResponse(pointHistory);
    }

    @Override
    @Transactional
    public EventResponse modifyEvent(EventRequest eventRequest) {
        PointHistoryRequest request = (PointHistoryRequest) eventRequest;

        // 수정 대상 = 사용자가 쓴 리뷰
        PointHistory recentHistory = pointHistoryRepository
                .findTopByReviewIdOrderByCreatedAtDesc(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Not Found History"));

        if (recentHistory.getAction() == ActionType.DELETE) {
            throw new CustomException(ExceptionType.BAD_REQUEST_ACTION);
        }
        PointHistory modifyHistory = request.toEntity();
        modifyHistory.modifyPoint(recentHistory.isFirstReview(), request, recentHistory.getCurrentPoint());

        pointHistoryRepository.save(modifyHistory);
        return new PointHistoryResponse(modifyHistory);
    }

    @Override
    @Transactional
    public EventResponse deleteEvent(EventRequest eventRequest) {
        PointHistoryRequest request = (PointHistoryRequest) eventRequest;

        // 최근 이력이 없음 == 삭제할 리뷰가 없음으로 판단 = 에러
        PointHistory targetHistory = pointHistoryRepository.findTopByReviewIdOrderByCreatedAtDesc(request.getReviewId())
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_REVIEW));

        PointHistory pointHistory = request.toEntity();
        pointHistory.deletePoint(targetHistory.getCurrentPoint(), targetHistory.isFirstReview());

        pointHistoryRepository.save(pointHistory);
        return new PointHistoryResponse(pointHistory);
    }
}
