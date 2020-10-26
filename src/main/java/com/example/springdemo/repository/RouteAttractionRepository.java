package com.example.springdemo.repository;

import com.example.springdemo.models.entity.Attraction;
import com.example.springdemo.models.entity.RouteAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteAttractionRepository extends JpaRepository<RouteAttraction, Long> {
    RouteAttraction findByRouteIdAndAttractionId(long routeId, long attractionId);

    List<RouteAttraction> findAllByRouteId(long id);
}
