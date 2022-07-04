package com.triple.point.proxy;

import com.triple.point.domain.common.dto.EventRequest;
import com.triple.point.domain.common.dto.EventResponse;
import com.triple.point.domain.points.entity.ActionType;
import com.triple.point.domain.points.entity.EventType;
import com.triple.point.domain.points.service.EventService;
import com.triple.point.domain.points.service.PointHistoryService;
import com.triple.point.domain.points.service.TestUserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


@Service
public class EventServiceProxy  {
    private Map<String, EventService> map = new HashMap<>();
    private final Map<String, String> methodMap = Map.of(
            "ADD", "addEvent",
            "MOD", "modifyEvent",
            "DELETE", "deleteEvent"
    );

    public EventServiceProxy(
            @Autowired PointHistoryService pointHistoryService,
            @Autowired TestUserEventService testUserEventService) {
        map.put("REVIEW", pointHistoryService);
        map.put("USER", testUserEventService);
    }

    public EventResponse invoke(EventType type, ActionType action, EventRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends EventService> eventServiceClass = map.get(type.name()).getClass();
        Method method = eventServiceClass.getMethod(methodMap.get(action.name()), EventRequest.class);

        return (EventResponse) method.invoke(map.get(type.name()), request);
    }

}
