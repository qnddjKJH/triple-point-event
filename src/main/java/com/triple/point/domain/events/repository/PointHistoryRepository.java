package com.triple.point.domain.events.repository;

import com.triple.point.domain.events.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {

    List<PointHistory> findByReviewId(String reviewId);

    List<PointHistory> findByUserId(String userId); // 유저별 내역
}
