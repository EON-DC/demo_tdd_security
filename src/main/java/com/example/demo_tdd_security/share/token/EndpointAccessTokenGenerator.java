package com.example.demo_tdd_security.share.token;

import java.util.List;

public interface EndpointAccessTokenGenerator {

    String createAccessToken(String email, List<String> roles);

    String createRefreshToken(String email, List<String> roles);
}
