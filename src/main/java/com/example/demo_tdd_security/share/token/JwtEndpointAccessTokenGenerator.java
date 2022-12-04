package com.example.demo_tdd_security.share.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtEndpointAccessTokenGenerator implements EndpointAccessTokenGenerator {

    private final long MINUTES = 60 * 1000L;
    private final long ACCESS_TOKEN_VALID_TIME = 10 * MINUTES;
    private final long REFRESH_TOKEN_VALID_TIME = 30 * MINUTES;
    private JwtSecretKey secretKey;

    public JwtEndpointAccessTokenGenerator(JwtSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String createAccessToken(String email, List<String> roles) {
        return createToken(email, roles, ACCESS_TOKEN_VALID_TIME);
    }

    @Override
    public String createRefreshToken(String email, List<String> roles) {
        return createToken(email, roles, REFRESH_TOKEN_VALID_TIME);
    }

    private String createToken(String email, List<String> roles, long validTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getSecretKeyAsBytes())
                .compact();
    }
}
