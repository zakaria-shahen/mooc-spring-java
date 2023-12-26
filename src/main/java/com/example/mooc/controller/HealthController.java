package com.example.mooc.controller;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    @GetMapping("/health/Authenticated")
    public Map<Object, Object> getStatus(JwtAuthenticationToken principal) {
        return Map.of(
                "status", principal.getToken().getClaim("user_id")
        );
    }
}
