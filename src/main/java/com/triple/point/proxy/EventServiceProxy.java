package com.triple.point.proxy;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.events.service.EventsService;
import com.triple.point.domain.events.service.EventsReviewPointService;
import com.triple.point.domain.events.service.ExampleUserEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventServiceProxy  {
    private Map<String, EventsService> serviceMap = new HashMap<>();
    private final Map<String, String> methodMap = Map.of(
            "ADD", "addEvent",
            "MOD", "modifyEvent",
            "DELETE", "deleteEvent"
    );

    public EventServiceProxy(
            @Autowired EventsReviewPointService eventsReviewPointService,
            @Autowired ExampleUserEventsService exampleUserEventService) {
        serviceMap.put("REVIEW", eventsReviewPointService);
        serviceMap.put("USER", exampleUserEventService);
    }

    public EventResponse invoke(EventType type, ActionType action, EventRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends EventsService> eventServiceClass = serviceMap.get(type.name()).getClass();
        Method method = eventServiceClass.getMethod(methodMap.get(action.name()), EventRequest.class);

        return (EventResponse) method.invoke(serviceMap.get(type.name()), request);
    }
}
