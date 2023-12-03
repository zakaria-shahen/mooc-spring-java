package com.example.mooc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain endpointsSecurity(HttpSecurity httpSecurity) throws Exception {
        // JWE have user id, user name, rule(user, admin)
        return httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.anyRequest().permitAll()
        ).build();
    }
}
