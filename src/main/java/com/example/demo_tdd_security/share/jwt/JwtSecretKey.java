package com.example.demo_tdd_security.share.jwt;

import java.nio.charset.StandardCharsets;

public class JwtSecretKey {

    private final byte[] secretKey;

    public JwtSecretKey(String secretKeyAsPhrase) {
        secretKey = secretKeyAsPhrase.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getSecretKeyAsByte() {
        return secretKey;
    }
}
