package com.triple.point.domain.points.dto;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.points.entity.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryRequest implements EventRequest {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    public PointHistory toEntity() {
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

    public boolean isContentLengthBonus() {
        return content.length() > 0;
    }

    public boolean isAttachedPhotoIds() {
        return attachedPhotoIds.size() > 0;
    }
}
