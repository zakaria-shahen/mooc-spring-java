package com.example.mooc.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!prod")
@AllArgsConstructor
public class SecurityConfig {

    private JwtDecoder cJweDecoder;

    @Bean
    public SecurityFilterChain endpointsSecurity(HttpSecurity httpSecurity) throws Exception {
        // rule(user, admin)
        return httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/bootcamp/**",
                                "/career/**",
                                // TODO: disable, if bootcamp API private API not a public API.
                                "/v3/api-docs",
                                "swagger-ui*/*"
                        ).permitAll()
                        .requestMatchers(
                                "/auth/**",
                                "/health"
                        ).permitAll()
                        .anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->  httpSecurityOAuth2ResourceServerConfigurer.jwt(
                        it -> it.decoder(cJweDecoder)
                )).build();
    }

}
