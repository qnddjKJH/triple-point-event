package com.triple.point.domain.points.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.points.entity.ActionType;
import com.triple.point.domain.points.entity.EventType;
import com.triple.point.domain.points.entity.PointHistory;
import lombok.Data;

import java.util.UUID;

@Data
public class PointHistoryResponse implements EventResponse {

    private UUID pointHistoryId;
    private EventType type;
    private ActionType action;
    private boolean firstReview;
    private Integer increasePoint;
    private Integer currentPoint;
    private String userId;
    private String placeId;
    private String reviewId;

    public PointHistoryResponse(PointHistory history) {
        this.pointHistoryId = history.getId();
        this.type = history.getType();
        this.action = history.getAction();
        this.firstReview = history.isFirstReview();
        this.increasePoint = history.getIncreasePoint();
        this.currentPoint = history.getCurrentPoint();
        this.userId = history.getUserId();
        this.placeId = history.getPlaceId();
        this.reviewId = history.getReviewId();
    }
}
