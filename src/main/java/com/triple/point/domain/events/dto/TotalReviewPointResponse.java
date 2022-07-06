package com.triple.point.domain.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TotalReviewPointResponse {
    private String userId;
    private int totalPoint = 0;
    private int reviewCount;
}
