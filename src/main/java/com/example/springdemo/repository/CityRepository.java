package com.example.springdemo.repository;

import com.example.springdemo.models.entity.City;
import com.example.springdemo.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>  {
    City findByName(String name);
}
