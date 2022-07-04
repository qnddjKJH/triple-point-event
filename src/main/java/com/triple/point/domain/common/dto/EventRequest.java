package com.triple.point.domain.common.dto;

import com.triple.point.domain.points.entity.ActionType;
import com.triple.point.domain.points.entity.EventType;

public interface EventRequest {
    EventType getType();
    ActionType getAction();
    <E> E toEntity();
}
