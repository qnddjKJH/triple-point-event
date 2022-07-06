package com.triple.point.domain.events.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;

public interface EventsService {
    EventResponse addEvent(EventRequest request);

    EventResponse modifyEvent(EventRequest request);

    EventResponse deleteEvent(EventRequest request);
}
