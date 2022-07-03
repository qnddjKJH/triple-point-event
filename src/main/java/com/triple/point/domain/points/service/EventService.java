package com.triple.point.domain.points.service;

public interface EventService<E> {
    E addEvent();

    E modifyEvent();

    E deleteEvent();
}
