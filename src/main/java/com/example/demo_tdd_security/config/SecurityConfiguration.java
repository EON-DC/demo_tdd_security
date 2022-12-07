package com.example.demo_tdd_security.config;

import com.example.demo_tdd_security.authentication.filter.JwtRequestFilter;
import com.example.demo_tdd_security.authentication.service.UserService;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtSecretKey secretKey;
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(JwtSecretKey secretKey, UserDetailsService userDetailsService) {
        this.secretKey = secretKey;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/signup", "/v2/**", "/swagger-ui/**", "/swagger-info/**"
                        , "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(userDetailsService, secretKey), UsernamePasswordAuthenticationFilter.class)
                // 만약 header에 token을 자동으로 추가하고 싶다면, UsernamePasswordAuthentication을 상속 받은 객체 생성하여 대체한다.
                .headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable()
                .and().build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
