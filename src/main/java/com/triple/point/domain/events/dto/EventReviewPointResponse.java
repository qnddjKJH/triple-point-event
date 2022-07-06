package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.events.entity.EventsReviewPoint;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventReviewPointResponse implements EventResponse {

    private UUID pointHistoryId;
    private EventType type;
    private ActionType action;
    private boolean firstReview;
    private Integer increasePoint;
    private Integer currentPoint;
    private String userId;
    private String placeId;
    private String reviewId;
    private LocalDateTime createdAt;

    public EventReviewPointResponse(EventsReviewPoint history) {
        this.pointHistoryId = history.getId();
        this.type = history.getType();
        this.action = history.getAction();
        this.firstReview = history.isFirstReview();
        this.increasePoint = history.getIncreasePoint();
        this.currentPoint = history.getCurrentPoint();
        this.userId = history.getUserId();
        this.placeId = history.getPlaceId();
        this.reviewId = history.getReviewId();
        this.createdAt = history.getCreatedAt();
    }
}