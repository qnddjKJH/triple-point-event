package com.triple.point.proxy;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.common.exception.CustomException;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.common.type.ExceptionType;
import com.triple.point.domain.events.service.EventsService;
import com.triple.point.domain.events.service.EventsReviewPointService;
import com.triple.point.domain.events.service.ExampleUserEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EventServiceProxy  {
    private Map<EventType, EventsService> serviceMap = new HashMap<>();
    private final Map<ActionType, String> methodMap = Map.of(
            ActionType.ADD, "addEvent",
            ActionType.MOD, "modifyEvent",
            ActionType.DELETE, "deleteEvent"
    );

    public EventServiceProxy(
            @Autowired EventsReviewPointService eventsReviewPointService,
            @Autowired ExampleUserEventsService exampleUserEventService) {
        serviceMap.put(EventType.REVIEW, eventsReviewPointService);
        serviceMap.put(EventType.USER, exampleUserEventService);
    }

    public EventResponse invoke(EventType type, ActionType action, EventRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        EventsService eventsService = Optional.ofNullable(serviceMap.get(type))
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST_TYPE, type));

        String methodName = Optional.ofNullable(methodMap.get(action))
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST_ACTION, action));

        Class<? extends EventsService> eventServiceClass = eventsService.getClass();
        Method method = eventServiceClass.getMethod(methodName, EventRequest.class);

        return (EventResponse) method.invoke(serviceMap.get(type), request);
    }
}
