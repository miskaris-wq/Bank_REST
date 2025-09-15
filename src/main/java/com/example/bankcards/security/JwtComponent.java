package com.example.bankcards.security;

import com.example.bankcards.exception.EmptyTokenException;
import com.example.bankcards.exception.InvalidTokenException;
import com.example.bankcards.exception.TokenExpiredException;
import com.example.bankcards.util.DecoderKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtComponent {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    private void decryptedKey(){
        key = DecoderKey.fromBase64(jwtSecret).getSecretKey();
    }

    public String generateJwtToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(key)
                .compact();
    }


    public String extractUserName(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getExpiration);
    }

    public void checkTokenOnEmpty(String token) {
        if (token == null || token.isBlank()) {
            throw new EmptyTokenException("Токен пустой");
        }
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolvers
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Срок действия токена истёк. Пожалуйста, авторизуйтесь заново");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new InvalidTokenException("Недействительный токен");
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractUserName(token);
        return (
                (login.equals(userDetails.getUsername())) && !isTokenExpired(token)
        );
    }

    protected boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
