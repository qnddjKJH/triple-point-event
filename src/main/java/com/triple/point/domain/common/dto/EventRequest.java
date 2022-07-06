package com.triple.point.domain.common.dto;

import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;

public interface EventRequest {
    EventType getType();
    ActionType getAction();
}
