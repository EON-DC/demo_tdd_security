package com.example.demo_tdd_security.authentication.filter;

import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.service.UserService;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtSecretKey jwtSecretKey;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtSecretKey jwtSecretKey) {
        this.userDetailsService = userDetailsService;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer != null) {
            String extractedEmail = Jwts.parser()
                    .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                    .parseClaimsJws(bearer)
                    .getBody().getSubject();
            UserDetails user = userDetailsService.loadUserByUsername(extractedEmail);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
