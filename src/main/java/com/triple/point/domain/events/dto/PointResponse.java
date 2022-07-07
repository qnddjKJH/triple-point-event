package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.common.type.ReviewType;
import com.triple.point.domain.events.entity.PointHistory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PointResponse implements EventResponse {

    private UUID pointHistoryId;
    private LocalDateTime createdAt;
    private EventType type;
    private ActionType action;
    private Integer increasePoint;
    private Integer historyUserTotal;
    private Integer historyReviewPoint;
    private ReviewType reviewType;
    private String userId;
    private String reviewId;
    private String placeId;
    public PointResponse(PointHistory history) {
        this.pointHistoryId = history.getId();
        this.createdAt = history.getCreatedAt();
        this.type = history.getType();
        this.action = history.getAction();
        this.increasePoint = history.getIncreasePoint();
        this.historyUserTotal = history.getUserTotal();
        this.historyReviewPoint = history.getReviewPoint();
        this.reviewType = history.getReviewType();
        this.userId = history.getUserId();
        this.reviewId = history.getReviewId();
        this.placeId = history.getPlaceId();
    }
}
