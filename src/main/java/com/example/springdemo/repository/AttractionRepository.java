package com.example.springdemo.repository;

import com.example.springdemo.models.entity.Attraction;
import com.example.springdemo.models.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    Attraction findByHashcode(String hashCode);

    @Query(name="select h from Attraction a join Route r where r.id = id")
    List<Attraction> findAllByRouteId(@Param("routeId") long id);
}
