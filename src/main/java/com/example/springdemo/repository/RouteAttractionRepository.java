package com.example.springdemo.repository;

import com.example.springdemo.models.entity.RouteAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteAttractionRepository extends JpaRepository<RouteAttraction, Long> {
    RouteAttraction findByRouteIdAndAttractionId(long routeId, long attractionId);
}
