package com.triple.point.domain.events.repository;

import com.triple.point.domain.events.entity.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPointRepository extends JpaRepository<UserPoint, UUID> {
}
