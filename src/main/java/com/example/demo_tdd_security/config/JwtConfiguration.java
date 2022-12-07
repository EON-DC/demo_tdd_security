package com.example.demo_tdd_security.config;

import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Bean
    JwtSecretKey jwtSecretKey() {
        return new JwtSecretKey("very_difficult_key");
    }
}
