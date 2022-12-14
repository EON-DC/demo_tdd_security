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

    private JwtSecretKey jwtSecretKey;
    private UserDetailsService userDetailsService;

    public SecurityConfiguration(JwtSecretKey jwtSecretKey, UserDetailsService userDetailsService) {
        this.jwtSecretKey = jwtSecretKey;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .addFilterBefore(new JwtRequestFilter(jwtSecretKey), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/signup", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
