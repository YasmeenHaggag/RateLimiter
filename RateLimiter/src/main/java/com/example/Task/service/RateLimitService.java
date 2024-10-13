package com.example.Task.service;

import com.example.Task.Exception.NotFoundException;
import com.example.Task.entity.RateLimit;
import com.example.Task.repository.RateLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    RateLimitRepository rateLimitRepository;
    private Long requestsCounter;

    public boolean checkLimit(int userId) throws Throwable {

        RateLimit userLimit = (RateLimit) rateLimitRepository.findUserLimitById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();

        String userKey = "user:" + userId;
        requestsCounter = operations.increment(userKey);

        if (requestsCounter == 1) {
            redisTemplate.expire(userKey, userLimit.getDuration(), TimeUnit.SECONDS);
        }

        if (requestsCounter > userLimit.getMaxRequests()){
            logUpnormalBehavior(userLimit);
            return false;
        }
        return true;
    }


    public void logUpnormalBehavior(RateLimit userLimit) {
            String logMessage = String.format(
                    "User with ID %d exceeded limit [%d requests ] : allowed: %d in %d seconds %n",
                    userLimit.getUserId(),requestsCounter,userLimit.getMaxRequests(), userLimit.getDuration()
                    );

            try (PrintWriter out = new PrintWriter(new FileWriter("Logs.txt", true))) {
                out.print(logMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




