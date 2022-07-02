package com.triple.point.domain.points.dto;

import com.triple.point.domain.points.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsRequest {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    public boolean isContentLengthBonus() {
        return content.length() > 0;
    }

    public boolean isAttachedPhotoIds() {
        return attachedPhotoIds.size() > 0;
    }
}
