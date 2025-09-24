package com.applab.sportsstats.sports_stats_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Application is working!";
    }
    
    @GetMapping("/")
    public String home() {
        return "Sports Stats API is running. GraphQL endpoint: /graphql";
    }
}