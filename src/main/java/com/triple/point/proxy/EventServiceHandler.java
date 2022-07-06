package com.triple.point.proxy;

import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.events.service.EventsService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EventServiceHandler implements InvocationHandler {
    private final EventsService eventsService;

    public EventServiceHandler(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return (EventResponse) method.invoke(eventsService, args);
    }
}
