package com.example.demo_tdd_security.share.jwt;

import com.example.demo_tdd_security.authentication.domain.UserRole;

import java.util.List;

public interface EndPointAccessTokenGenerator {

    String createAccessToken(String email, List<UserRole> roles);

    String createRefreshToken(String email, List<UserRole> roles);
}
