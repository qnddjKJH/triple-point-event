package com.triple.point.domain.points.repository;

import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.points.entity.PointHistory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
    Optional<PointHistory> findByUserIdAndPlaceIdAndAction(String userId, String placeId, ActionType action);
    Optional<PointHistory> findTopByReviewIdOrderByCreatedAtDesc(String reviewId);
    List<PointHistory> findByUserId(String userId); // 유저별 내역
    List<PointHistory> findByPlaceId(String placeId); // 장소별
    List<PointHistory> findByReviewId(String reviewId); // 리뷰별
}
