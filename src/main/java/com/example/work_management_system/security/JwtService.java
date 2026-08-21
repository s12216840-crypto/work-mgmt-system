package com.example.work_management_system.security;

import com.example.work_management_system.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        this.secretKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secret)
                );

        this.expiration = expiration;
    }

    public String generateToken(
            String email,
            Role role) {

        Date now = new Date();

        Date expiry =
                new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {

        return getClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            String username) {

        try {

            Claims claims = getClaims(token);

            return claims.getSubject().equals(username)
                    && claims.getExpiration()
                    .after(new Date());

        } catch (Exception exception) {
            return false;
        }
    }

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}