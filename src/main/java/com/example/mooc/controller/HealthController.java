package com.example.mooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<Object, Object> getStatus() {
        return Map.of(
                "status", "stable"
        );
    }
}
