package com.triple.point.domain.points.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;

public interface EventService {
    EventResponse addEvent(EventRequest request);

    EventResponse modifyEvent(EventRequest request);

    EventResponse deleteEvent(EventRequest request);
}
