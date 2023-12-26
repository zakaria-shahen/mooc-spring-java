package com.example.mooc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import static com.example.mooc.security.JweService.EXPIRATION_AFTER_SECONDS;

@RedisHash(timeToLive = EXPIRATION_AFTER_SECONDS)
public record AccessTokenBlockList(
        @Id
        String accessToken
) {

}

