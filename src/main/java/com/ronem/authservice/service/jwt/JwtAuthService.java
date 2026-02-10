/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:22/12/2025
 * Time:13:27
 */


package com.ronem.authservice.service.jwt;

import com.ronem.authservice.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * Class responsible for jwt token activity
 */
@Service
@RequiredArgsConstructor
public class JwtAuthService {

    //Token Types
    private static final String CLAIM_TOKEN_TYPE = "token_type";
    private static final String ACCESS = "ACCESS";
    private static final String REFRESH = "REFRESH";

    //secret
    @Value("${jwt.access-secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    //Access Token
    private SecretKey accessTokenKey;
    // expire after 5 minutes
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 5 * 60 * 1000; // 5 min

    //Refresh Token
    private SecretKey refreshTokenKey;
    // expires after 7 days
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;


    //construction injection for the access and refresh secret
    //it will be picked from application.yaml file
    @PostConstruct
    public void init() {
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
    }


    //Method to create JWT token
    public String createToken(User entity) {
        return Jwts.builder()
                .subject(entity.getId().toString())
                .claim("role", entity.getUserRole().name())
                .claim(CLAIM_TOKEN_TYPE, ACCESS)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(this.accessTokenKey)
                .compact();
    }


    //Method to create Refresh Token
    public String createRefreshToken(User entity) {
        return Jwts.builder()
                .subject(entity.getId().toString())
                .id(UUID.randomUUID().toString())
                .claim(CLAIM_TOKEN_TYPE, REFRESH)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(this.refreshTokenKey)
                .compact();
    }

    // =========================
    // VALIDATION
    // =========================
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessTokenKey, ACCESS);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshTokenKey, REFRESH);
    }

    private boolean validateToken(String token, SecretKey key, String expectedType) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
            return expectedType.equals(tokenType);

        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    //return claims.getSubject() because during jwt creation we passed subject as mobile number and which is userId
    public Long getUserIdFromAccessToken(String accessToken) {
        Claims claims = extractClaims(accessToken, accessTokenKey);
        return Long.parseLong(claims.getSubject());
    }


    public String getRoleFromAccessToken(String token) {
        return extractClaims(token, accessTokenKey).get("role", String.class);
    }

    private Claims extractClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}