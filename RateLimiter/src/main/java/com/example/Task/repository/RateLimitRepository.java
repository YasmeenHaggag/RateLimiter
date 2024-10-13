package com.example.Task.repository;

import com.example.Task.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit,Integer> {
        @Query("select r from RateLimit r where r.userId= :userId")
        Optional findUserLimitById(@Param("userId") int userId);
}
