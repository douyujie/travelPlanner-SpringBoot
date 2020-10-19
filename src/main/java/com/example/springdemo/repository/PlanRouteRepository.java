package com.example.springdemo.repository;

import com.example.springdemo.models.entity.PlanRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRouteRepository extends JpaRepository<PlanRoute, Long> {
}
