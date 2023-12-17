package com.example.mooc.security;

import com.example.mooc.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class JweService {

    private JweService() {}

    private static final SecretKey SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode("yLZaJx1QSOjJ6q7kJhjuJ7JMGV9uDSy7quRYvvcSV9E="), 0, 32, "AES");
    private static final AeadAlgorithm AEAD_ALGORITHM = Jwts.ENC.A256GCM;
    private static final long EXPIRATION_AFTER_SECONDS = 600L;
    private static final JwtParser parser = Jwts.parser().decryptWith(SECRET_KEY).build();

    public static String generateToken(UserModel user) {
        return Jwts.builder().claims()
                .issuer("mooc")
                .add("roles", user.getRole())
                // OpenID Claims
                .add("name", user.getName())
                .add("email", user.getEmail())
                // Custom Claims
                .add("user_id", user.getId())
                .and()
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_AFTER_SECONDS)))
                .encryptWith(SECRET_KEY, AEAD_ALGORITHM)
                .compact();

    }

    public static Claims validateTokenAndReturnClaims(String token) {
        return parser.parseEncryptedClaims(token).getPayload();
    }

}
