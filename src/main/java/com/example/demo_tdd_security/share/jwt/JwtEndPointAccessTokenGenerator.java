package com.example.demo_tdd_security.share.jwt;

import com.example.demo_tdd_security.authentication.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;

public class JwtEndPointAccessTokenGenerator implements EndPointAccessTokenGenerator {

    private final long MINUTES = 60 * 1000L;
    private final long ACCESS_TOKEN_VALID_TIME = 10 * MINUTES;
    private final long REFRESH_TOKEN_VALID_TIME = 30 * MINUTES;

    private final  JwtSecretKey secretKey;

    public JwtEndPointAccessTokenGenerator(JwtSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String createAccessToken(String email, List<UserRole> roles) {
        return createToken(email, roles, ACCESS_TOKEN_VALID_TIME);
    }

    @Override
    public String createRefreshToken(String email, List<UserRole> roles) {
        return createToken(email, roles, REFRESH_TOKEN_VALID_TIME);
    }

    private String createToken(String email, List<UserRole> roles, long invalidTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + invalidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getSecretKeyAsBytes())
                .compact();

    }
}
