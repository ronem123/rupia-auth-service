/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:22/12/2025
 * Time:13:27
 */


package com.ronem.authservice.service.jwt;

import com.ronem.authservice.model.entity.User;
import com.ronem.rupiasecuritylib.properties.JwtProperties;
import com.ronem.rupiasecuritylib.service.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Class responsible for jwt token activity
 */
@Service
@RequiredArgsConstructor
public class JwtTokenGeneratorService {

    //secret
//    @Value("${jwt.access-secret}")
//    private String accessSecret;
//
//    @Value("${jwt.refresh-secret}")
//    private String refreshSecret;

    private final JwtProperties jwtProperties;
    private final JwtUtil jwtUtil;

    //Access Token
//    private SecretKey accessTokenKey;
    // expire after 5 minutes

    //Refresh Token
//    private SecretKey refreshTokenKey;
    // expires after 7 days


    //construction injection for the access and refresh secret
    //it will be picked from application.yaml file
//    @PostConstruct
//    public void init() {
//        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
//        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
//    }


    //Method to create JWT token
    public String createToken(User entity) {
        return Jwts.builder()
                .subject(entity.getId().toString())
                .claim(JwtUtil.CLAIM_ROLE, entity.getUserRole().name())
                .claim(JwtUtil.CLAIM_TOKEN_TYPE, JwtUtil.ACCESS)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.ACCESS_TOKEN_EXPIRATION))
                .signWith(jwtUtil.getAccessTokenSecretKey())
                .compact();
    }


    //Method to create Refresh Token
    public String createRefreshToken(User entity) {
        return Jwts.builder()
                .subject(entity.getId().toString())
                .id(UUID.randomUUID().toString())
                .claim(JwtUtil.CLAIM_TOKEN_TYPE, JwtUtil.REFRESH)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.REFRESH_TOKEN_EXPIRATION))
                .signWith(jwtUtil.getAccessTokenSecretKey())//modify later with refresh secret key
                .compact();
    }

}