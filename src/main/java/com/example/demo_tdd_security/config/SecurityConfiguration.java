package com.example.demo_tdd_security.config;

import com.example.demo_tdd_security.authentication.filter.JwtRequestFilter;
import com.example.demo_tdd_security.share.token.JwtSecretKey;
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

    private final UserDetailsService userDetailsService;

    private final JwtSecretKey jwtSecretKey;

    public SecurityConfiguration(UserDetailsService userDetailsService, JwtSecretKey jwtSecretKey) {
        this.userDetailsService = userDetailsService;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login/**", "/signup/**", "/swagger-ui/**","/v2/**", "/webjars/**", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(userDetailsService, jwtSecretKey),
                        UsernamePasswordAuthenticationFilter.class)
                .headers(header -> header.frameOptions().sameOrigin().httpStrictTransportSecurity().disable())
                .build();
    }
}
