package com.example.mooc.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;

@Component
public class CJweDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        var jwe = JweService.validateTokenAndReturnJwe(token);
        var claims = new HashMap<>(jwe.getPayload());
        claims.replace(JwtClaimNames.EXP, Instant.ofEpochSecond((Long) claims.get(JwtClaimNames.EXP)));
        return Jwt.withTokenValue(token)
                .claims(it -> it.putAll(claims))
                .headers(it -> it.putAll(jwe.getHeader()))
                .build();
    }
}
