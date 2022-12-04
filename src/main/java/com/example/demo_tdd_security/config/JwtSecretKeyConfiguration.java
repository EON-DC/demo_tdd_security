package com.example.demo_tdd_security.config;


import com.example.demo_tdd_security.share.token.JwtSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecretKeyConfiguration {

    @Bean
    public JwtSecretKey secretKey() {
        return new JwtSecretKey("veryDifficultSecret");
    }
}
