package com.example.springdemo.repository;

import com.example.springdemo.models.History;
import com.example.springdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository <History, Long> {
    @Query(name="select h from History h join User u where u.username = user.username")
    List<History> findAllByUser(@Param("user") User user);
}
