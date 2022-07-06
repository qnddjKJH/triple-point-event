package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.events.entity.PointHistory;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.domain.events.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest implements EventRequest {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    public User toUser() {
        return User.createUser(this);
    }

    public Review toNormalReview() {
        return Review.createReview(this);
    }

    public Review toFirstReview() {
        return Review.createFirstReview(this);
    }

    public PointHistory toHistory() {
        return PointHistory.createPointHistory(this);
    }

    @Override
    public EventType getType() {
        return EventType.valueOf(type);
    }

    @Override
    public ActionType getAction() {
        return ActionType.valueOf(action);
    }
}
