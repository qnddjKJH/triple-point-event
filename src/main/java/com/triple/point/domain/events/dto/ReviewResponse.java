package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.type.ReviewType;
import com.triple.point.domain.events.entity.Review;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewResponse implements EventResponse {
    private UUID reviewId;
    private String content;
    private int photoCount;
    private ReviewType type;
    private int point;
    private UserResponse user;

    public ReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.photoCount = review.getAttachedPhotoIds();
        this.type = review.getType();
        this.point = review.getPoint();
        this.user = new UserResponse(review.getUser());
    }
}
