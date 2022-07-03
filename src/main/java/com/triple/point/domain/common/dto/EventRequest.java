package com.triple.point.domain.common.dto;

public interface EventRequest<E> {
    E toEntity();
}
