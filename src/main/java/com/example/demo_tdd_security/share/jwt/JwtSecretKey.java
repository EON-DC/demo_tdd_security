package com.example.demo_tdd_security.share.jwt;

public class JwtSecretKey {

    private final byte[] secretKey;

    public JwtSecretKey(String secretKeyWordPhase) {
        this.secretKey = secretKeyWordPhase.getBytes();
    }

    public byte[] getSecretKeyAsBytes() {
        return secretKey;
    }
}
