package com.example.springdemo.repository;

import com.example.springdemo.models.entity.Route;
import com.example.springdemo.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByHashcode(String hashCode);
}
