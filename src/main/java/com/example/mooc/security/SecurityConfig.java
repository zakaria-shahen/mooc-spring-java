package com.example.mooc.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!prod")
@AllArgsConstructor
public class SecurityConfig {

    private JwtDecoder cJweDecoder;

    @Bean
    public SecurityFilterChain endpointsSecurity(HttpSecurity httpSecurity) throws Exception {
        // JWE have user id, user name, rule(user, admin)
        return httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers(
                                "/auth/**",
                                "/health"
                        ).anonymous()
                        .anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->  httpSecurityOAuth2ResourceServerConfigurer.jwt(
                        it -> it.decoder(cJweDecoder)
                )).build();
    }

}
