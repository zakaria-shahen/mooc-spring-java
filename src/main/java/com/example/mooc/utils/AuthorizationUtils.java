package com.example.mooc.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static com.example.mooc.security.JweService.ClaimNames.USER_ID;

public class AuthorizationUtils {

    private AuthorizationUtils() {
    }

    public static Long getUserId(JwtAuthenticationToken principal) {
        return Long.valueOf((int) principal.getTokenAttributes().get(USER_ID));
    }

    public static boolean isAdmin(JwtAuthenticationToken principal) {
        return principal.getAuthorities().stream().anyMatch(it -> it.getAuthority().contains("SCOPE_ADMIN"));
    }

    public static boolean isNotAdmin(JwtAuthenticationToken principal) {
        return !isAdmin(principal);
    }
}
