package com.triple.point.domain.events.dto;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.events.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse implements EventResponse {
    private UUID userId;
    private int total;
    private int reviewCount;

    public UserResponse(User user) {
        this.userId = userId;
        this.total = total;
        this.reviewCount = reviewCount;
    }
}
