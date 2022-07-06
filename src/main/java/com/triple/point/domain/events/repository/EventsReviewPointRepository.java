package com.triple.point.domain.events.repository;

import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.events.entity.EventsReviewPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventsReviewPointRepository extends JpaRepository<EventsReviewPoint, UUID> {
    Optional<EventsReviewPoint> findByUserIdAndPlaceIdAndAction(String userId, String placeId, ActionType action);
    Optional<EventsReviewPoint> findTopByReviewIdOrderByCreatedAtDesc(String reviewId);
    List<EventsReviewPoint> findByUserId(String userId); // 유저별 내역
    List<EventsReviewPoint> findByPlaceId(String placeId); // 장소별
    List<EventsReviewPoint> findByReviewId(String reviewId); // 리뷰별
}
