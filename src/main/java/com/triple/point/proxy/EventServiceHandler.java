package com.triple.point.proxy;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.points.service.EventService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EventServiceHandler implements InvocationHandler {
    private final EventService eventService;

    public EventServiceHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return (EventResponse) method.invoke(eventService, args);
    }
}
