package com.example.springdemo.repository;

import com.example.springdemo.models.entity.PlanRoute;
import com.example.springdemo.models.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRouteRepository extends JpaRepository<PlanRoute, Long> {
    List<PlanRoute> findAllByPlanId(long id);
}
