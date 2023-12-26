package com.example.mooc.security;

import com.example.mooc.exception.AuthExceedLoginAttemptsException;
import com.example.mooc.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class JweService {

    private JweService() {
    }

    enum EncryptInfo {
        ACCESS_TOKEN(Jwts.ENC.A256GCM, new SecretKeySpec(Base64.getDecoder().decode("yLZaJx1QSOjJ6q7kJhjuJ7JMGV9uDSy7quRYvvcSV9E="), 0, 32, "AES")),
        REFRESH_TOKEN(Jwts.ENC.A256GCM, new SecretKeySpec(Base64.getDecoder().decode("yLZaJx1QSOjJ6q7kJhjuJ7JMGV9uDSy7quRYvvcSV9E="), 0, 32, "AES"));

        private final AeadAlgorithm aeadAlgorithm;
        private final SecretKey secretKey;

        EncryptInfo(AeadAlgorithm aeadAlgorithm, SecretKey secretKey) {
            this.aeadAlgorithm = aeadAlgorithm;
            this.secretKey = secretKey;
        }

    }

    static final int REFRESH_TOKEN_COUNT = 100;
    static final String REFRESH_TOKEN = "refresh_token";
    public static final long EXPIRATION_AFTER_SECONDS = 600L;
    private static final JwtParser parserAccessToken = Jwts.parser().decryptWith(EncryptInfo.ACCESS_TOKEN.secretKey).build();
    private static final JwtParser parserRefreshToken = Jwts.parser().decryptWith(EncryptInfo.REFRESH_TOKEN.secretKey).build();

    public static String generateAccessToken(UserModel user) {
        return Jwts.builder().claims()
                .issuer("mooc")
                .add("scope", user.getRole())
                // OpenID Claims
                .add("name", user.getName())
                .add("email", user.getEmail())
                // Custom Claims
                .add("user_id", user.getId())
                .and()
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_AFTER_SECONDS)))
                .encryptWith(EncryptInfo.ACCESS_TOKEN.secretKey, EncryptInfo.ACCESS_TOKEN.aeadAlgorithm)
                .compact();

    }


    public static String generateAccessToken(Claims refreshTokenClaims) {
        var accessTokenClaims = new HashMap<>(refreshTokenClaims);
        accessTokenClaims.remove(REFRESH_TOKEN);
        return Jwts.builder().claims()
                .add(accessTokenClaims)
                .and()
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_AFTER_SECONDS)))
                .encryptWith(EncryptInfo.ACCESS_TOKEN.secretKey, EncryptInfo.ACCESS_TOKEN.aeadAlgorithm)
                .compact();

    }

    public static String generateRefreshToken(UserModel user) {
        final Instant notBefore = Instant.now().plusSeconds(EXPIRATION_AFTER_SECONDS);
        return Jwts.builder().claims()
                .issuer("mooc")
                .add("roles", user.getRole())
                // OpenID Claims
                .add("name", user.getName())
                .add("email", user.getEmail())
                // Custom Claims
                .add("user_id", user.getId())
                .add(REFRESH_TOKEN, REFRESH_TOKEN_COUNT)
                .and()
                .notBefore(Date.from(notBefore))
                .expiration(Date.from(notBefore.plusSeconds(EXPIRATION_AFTER_SECONDS)))
                .encryptWith(EncryptInfo.REFRESH_TOKEN.secretKey, EncryptInfo.REFRESH_TOKEN.aeadAlgorithm)
                .compact();

    }

    public static String generateRefreshToken(Claims claims) {
        var refreshTokenCount = (int) claims.get(REFRESH_TOKEN);
        if (refreshTokenCount == 0) {
            //todo
            throw new AuthExceedLoginAttemptsException();
        }
        return Jwts.builder().claims()
                .add(claims)
                .add(REFRESH_TOKEN, refreshTokenCount - 1)
                .and()
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_AFTER_SECONDS)))
                .encryptWith(EncryptInfo.ACCESS_TOKEN.secretKey, EncryptInfo.ACCESS_TOKEN.aeadAlgorithm)
                .compact();
    }

    public static Claims validateRefreshTokenAndGetClaims(String token) {
        return parserRefreshToken.parseEncryptedClaims(token).getPayload();
    }

    public static Jwe<Claims> validateAccessTokenAndGetJwe(String token) {
        return parserAccessToken.parseEncryptedClaims(token);
    }

}
