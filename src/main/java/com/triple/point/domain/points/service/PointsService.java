package com.triple.point.domain.points.service;

import com.triple.point.domain.points.dto.PointsRequest;
import com.triple.point.domain.points.repository.PointsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PointsService {

    private final PointsRepository pointsRepository;

    /*
    * Points 기능
    * 사용자 별 총점 조회 *필수
    * 포인트 내역 조회 *필수
    * 포인트 추가 (action == ADD) *필수
    *  - 첫 리뷰 1점
    *  - 리뷰글 1자 이상 1점
    *  - 사진 1개 이상 1점
    * 포인트 수정 (action == MOD) *필수
    *  - 변경 리뷰글자수 0자 -1점
    *  - 변경 사진수 1개 -1점
    * 포인트 삭제 (action == DELETE) *필수
    *  - 모든 포인트 회수
    * 사용자별 포인트 내역
    *
    * 사용자는 장소마다 1개의 리뷰를 작성가능 *확인
    *
    *
    * 포인트 이력은 남아야 한다. *삭제 금지*
    */

    public void pointsServiceManager(PointsRequest request) {
        String action = request.getAction();

        if(action.equals("ADD")){
            addEvent(request);
        } else if (action.equals("MOD")) {
            modifyEvent(request);
        } else if (action.equals("DELETE")) {
            deleteEvent(request);
        } else {

        }
    }

    public void addEvent(PointsRequest request) {

    }

    public void modifyEvent(PointsRequest request) {

    }

    public void deleteEvent(PointsRequest request) {

    }
}
