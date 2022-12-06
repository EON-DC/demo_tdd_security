package com.example.demo_tdd_security.config;


import com.example.demo_tdd_security.authentication.filter.JwtRequestFilter;
import com.example.demo_tdd_security.share.token.JwtSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtSecretKey secretKey;

    public SecurityConfig(UserDetailsService userDetailsService, JwtSecretKey secretKey) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/swagger-ui/**", "v2/**", "/swagger-resources/**"
                        , "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(userDetailsService, secretKey)
                        , UsernamePasswordAuthenticationFilter.class)
                .headers(header -> header.frameOptions().sameOrigin().httpStrictTransportSecurity().disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
