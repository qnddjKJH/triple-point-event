package com.triple.point.domain.points.service;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.entity.ActionType;
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
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    /*
    * Points 기능
    * 사용자 별 총점 조회 *필수
    * 포인트 내역 조회 *필수
    * 포인트 추가 (action == ADD) *필수
    *  - 특정 장소에서의 첫 리뷰 1점 ==> 장소Id 조회 내역 없으면 1점
    *  - 리뷰글 1자 이상 1점 ==> content.length > 0 이면 1점
    *  - 사진 1개 이상 1점 ==> attachedPhotoIds.size > 0 이면 1점
    * 포인트 수정 (action == MOD) *필수
    *  - 변경 리뷰글자수 0자 -1점 ==> content.length == 0 이면 -1 or content.length > 0 이면 1점
    *  - 변경 사진수 1개 -1점 ==> attachedPhotoIds.size == 0 이고 기존 > 0 이면 -1 or attachedPhotoIds.size > 0 1점
    * 포인트 삭제 (action == DELETE) *필수
    *  - 모든 포인트 회수
    * 사용자별 포인트 내역
    *
    * 사용자는 장소마다 1개의 리뷰를 작성가능 *확인
    *
    *
    * 포인트 이력은 남아야 한다. *삭제 금지*
    */
    @Transactional
    public PointHistoryResponse pointsServiceManager(PointHistoryRequest request) {
        log.info(">>> Request data : {}", request);
        String action = request.getAction();

        if(action.equals("ADD")){
            log.info("start ADD event method : addEvent() ");
            return addEvent(request);
        } else if (action.equals("MOD")) {
            log.info("start MOD event method : modifyEvent() ");
            return modifyEvent(request);
        } else if (action.equals("DELETE")) {
            log.info("start DELETE event method : deleteEvent() ");
            return deleteEvent(request);
        } else {
            throw new RuntimeException("요청이 잘 못 되었습니다.");
        }
    }

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
    private PointHistoryResponse addEvent(PointHistoryRequest request) {
        // 특정 장소의 첫 리뷰 판단
        boolean checkFirstReview = pointHistoryRepository.findByPlaceId(request.getPlaceId()).isEmpty();

        // 장소에 해당 유저의 리뷰 내역이 있는지 조회
        // ADD 만 검색 (MOD 와 DELETE 내역들은 ADD 내역이 없으면 성립 불가)
        PointHistory pointHistory = pointHistoryRepository
                .findByUserIdAndPlaceIdAndAction(
                        request.getUserId(),
                        request.getPlaceId(),
                        ActionType.valueOf(request.getAction()))
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
        }

        // Todo : 응답에 포인트를 이미 받은 내역이 있다면 BAD_REQUEST
        return new PointHistoryResponse(pointHistory);
    }

    private PointHistoryResponse modifyEvent(PointHistoryRequest request) {
        // 수정 대상 = 사용자가 쓴 리뷰
        PointHistory recentHistory = pointHistoryRepository
                .findTopByUserIdAndReviewIdOrderByCreatedAtDesc(request.getUserId(), request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Not Found History"));

        // Todo : BAD_REQUEST 삭제 되었는데 수정을 하고 있음
//        if (pointHistory.getAction() == ActionType.DELETE) {
//            
//        }
        PointHistory modifyHistory = request.toEntity();
        modifyHistory.modifyPoint(recentHistory.isFirstReview(), request, recentHistory.getCurrentPoint());

        pointHistoryRepository.save(modifyHistory);
        return new PointHistoryResponse(modifyHistory);
    }

    private PointHistoryResponse deleteEvent(PointHistoryRequest request) {
        // 최근 이력이 없음 == 삭제할 리뷰가 없음으로 판단 = 에러
        PointHistory targetHistory = pointHistoryRepository.findTopByUserIdAndReviewIdOrderByCreatedAtDesc(request.getUserId(), request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Not Found"));

        PointHistory pointHistory = request.toEntity();
        pointHistory.deletePoint(targetHistory.getCurrentPoint(), targetHistory.isFirstReview());

        return new PointHistoryResponse(pointHistory);
    }
}
