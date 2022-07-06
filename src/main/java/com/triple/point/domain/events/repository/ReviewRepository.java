package com.triple.point.domain.events.repository;

import com.triple.point.domain.events.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    Optional<Review> findById(UUID uuid);

    List<Review> findByPlaceId(String placeId);

    @Query("select r from Review r join fetch r.user user where user.id = :userId and r.placeId = :placeId")
    Optional<Review> findByPlaceIdAndUser(@Param("placeId") String placeId, @Param("userId") UUID userId);
}
