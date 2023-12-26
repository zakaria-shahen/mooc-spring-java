package com.example.mooc.security;

import com.example.mooc.exception.AuthInvalidException;
import com.example.mooc.repository.AccessTokenBlockListRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;

@Component
@AllArgsConstructor
public class CJweDecoder implements JwtDecoder {

    private AccessTokenBlockListRepo accessTokenBlockListRepo;

    @Override
    public Jwt decode(String token) throws JwtException {
        if (accessTokenBlockListRepo.existsById(token)) {
             throw new AuthInvalidException();
        }
        var jwe = JweService.validateAccessTokenAndGetJwe(token);
        var claims = new HashMap<>(jwe.getPayload());
        claims.replace(JwtClaimNames.EXP, Instant.ofEpochSecond((Long) claims.get(JwtClaimNames.EXP)));
        return Jwt.withTokenValue(token)
                .claims(it -> it.putAll(claims))
                .headers(it -> it.putAll(jwe.getHeader()))
                .build();
    }
}
