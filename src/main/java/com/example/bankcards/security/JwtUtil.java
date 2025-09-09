package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtUtil(
            @Value("${app.security.jwt-secret}") String secret,
            @Value("${app.security.jwt-expiration-min:60}") long expirationMinutes
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("app.security.jwt-secret must be at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .subject(username)
                .claims(Map.of("roles", roles))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key) // 0.12.x: алгоритм определяется по ключу (HS256 для HMAC-ключа)
                .compact();
    }

    public String extractUsername(String token) {
        return parseSigned(token).getPayload().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) parseSigned(token).getPayload().get("roles");
    }

    public boolean isTokenValid(String token, UserDetails user) {
        try {
            Claims claims = parseSigned(token).getPayload();
            String username = claims.getSubject();
            Date exp = claims.getExpiration();
            return username.equals(user.getUsername()) && exp.after(new Date());
        } catch (RuntimeException e) {
            return false;
        }
    }

    private Jws<Claims> parseSigned(String token) {
        // 0.12.x: parser() + verifyWith(key) + parseSignedClaims(...)
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
