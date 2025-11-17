package com.example.SmartEventManagementSystem.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

   private static final String secret_key="A real mathematician can do mathematical mathematiculation";
    private final long EXPIRATION = 1000 * 60 * 60 ; // 1 hour
    private final Key secretKey = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));

    public String extractEmail(String token) {
        return Jwts.parserBuilder().build()
                .setSigningKey(secretKey).parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
}

    public boolean isValidToken(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}

