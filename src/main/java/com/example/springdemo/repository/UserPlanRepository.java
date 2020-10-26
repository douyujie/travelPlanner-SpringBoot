package com.example.springdemo.repository;

import com.example.springdemo.models.entity.Plan;
import com.example.springdemo.models.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    UserPlan findByUserIdAndPlanId(long userId, long planId);

    List<UserPlan> findAllByUserId(long userId);

}
