package com.triple.point.domain.points.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TotalPointResponse {
    private String userId;
    private int totalPoint;
}
