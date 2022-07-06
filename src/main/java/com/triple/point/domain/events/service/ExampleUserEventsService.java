package com.triple.point.domain.events.service;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExampleUserEventsService implements EventsService {

    @Override
    public EventResponse addEvent(EventRequest request) {
        log.info("this  => {}", this.getClass());
        return null;
    }

    @Override
    public EventResponse modifyEvent(EventRequest request) {
        log.info("this  => {}", this.getClass());
        return null;
    }

    @Override
    public EventResponse deleteEvent(EventRequest request) {
        log.info("this  => {}", this.getClass());
        return null;
    }
}
