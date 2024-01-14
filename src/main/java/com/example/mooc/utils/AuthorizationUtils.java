package com.example.mooc.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthorizationUtils {

    private AuthorizationUtils() {
    }

    public static Long getUserId(JwtAuthenticationToken principal) {
        return (Long) principal.getTokenAttributes().get("USER_ID");
    }

    public static boolean isNotAdmin(JwtAuthenticationToken principal) {
        return principal.getAuthorities().stream().anyMatch(it -> it.getAuthority().contains("ROLE_ADMIN"));
    }

    public static boolean isAdmin(JwtAuthenticationToken principal) {
        return !isNotAdmin(principal);
    }
}
