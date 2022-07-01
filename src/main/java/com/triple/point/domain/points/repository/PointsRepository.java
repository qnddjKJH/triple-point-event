package com.triple.point.domain.points.repository;

import com.triple.point.domain.points.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointsRepository extends JpaRepository<Points, UUID> {
}
