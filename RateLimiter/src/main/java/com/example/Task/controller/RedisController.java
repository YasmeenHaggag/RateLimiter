package com.example.Task.controller;

import com.example.Task.entity.Products;
import com.example.Task.service.ProductsService;
import com.example.Task.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedisController {

    @Autowired
    RateLimitService rateLimitService;

    @Autowired
    ProductsService productsService;


    @GetMapping("/rateLimit/{userId}")
    public ResponseEntity<?> CheckRateLimit(@PathVariable int userId) throws Throwable {

        if ( rateLimitService.checkLimit(userId) ) {
            List<Products> products = productsService.findAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Rate Limit exceeded.", HttpStatus.TOO_MANY_REQUESTS);
    }
}
