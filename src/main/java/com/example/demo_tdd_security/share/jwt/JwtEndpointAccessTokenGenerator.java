package com.example.demo_tdd_security.share.jwt;

import com.example.demo_tdd_security.authentication.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtEndpointAccessTokenGenerator implements EndpointAccessTokenGenerator{

    private final JwtSecretKey secretKey;

    private static final Long MINUTE = 60 * 1000L;
    private static final Long VALID_ACCESS_TOKEN_TIME = 10 * MINUTE;
    private static final Long VALID_REFRESH_TOKEN_TIME = 30 * MINUTE;

    public JwtEndpointAccessTokenGenerator(JwtSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String createAccessToken(String email, List<UserRole> roles) {
        return createToken(email, roles, VALID_ACCESS_TOKEN_TIME);
    }

    @Override
    public String createRefreshToken(String email, List<UserRole> roles) {
        return createToken(email, roles, VALID_REFRESH_TOKEN_TIME);
    }

    private String createToken(String email, List<UserRole> roles, Long validTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getSecretKeyAsByte())
                .compact();


    }
}
