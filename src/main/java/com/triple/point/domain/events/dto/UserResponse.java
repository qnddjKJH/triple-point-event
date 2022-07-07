package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.events.entity.User;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserResponse implements EventResponse {
    private UUID userId;
    private int total;
    private int reviewCount;
    private List<ReviewResponse> reviews;
    public UserResponse(User user) {
        this.userId = user.getId();
        this.total = user.getPoint();
        this.reviewCount = user.getReviews().size();
        this.reviews = user.getReviews().stream().map(ReviewResponse::new).collect(Collectors.toList());
    }
}
