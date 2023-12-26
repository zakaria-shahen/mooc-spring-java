package com.example.mooc.dto.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        OAuth2AccessToken.TokenType tokenType,
        Long expires // number of seconds

) {

    @JsonGetter
    public String getTokenType() {
        return tokenType.getValue();
    }
}
