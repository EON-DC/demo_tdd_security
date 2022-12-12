package com.example.demo_tdd_security.authentication.filter;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.service.DefaultUserService;
import com.example.demo_tdd_security.share.jwt.EndPointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtEndPointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTests {

    private BCryptPasswordEncoder passwordEncoder;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private MockFilterChain mockFilterChain;
    private UserDetailsService mockUserDetailsService;
    private User user;
    private JwtRequestFilter filterToTest;
    private JwtSecretKey jwtSecretKey;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();
        mockUserDetailsService = mock(UserDetailsService.class);

        user = User.builder()
                .name("john")
                .email("john@email.com")
                .password(passwordEncoder.encode("password"))
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build();

        jwtSecretKey = new JwtSecretKey("secretTest");
        filterToTest = new JwtRequestFilter(mockUserDetailsService, jwtSecretKey);
        JwtEndPointAccessTokenGenerator jwtEndPointAccessTokenGenerator
                = new JwtEndPointAccessTokenGenerator(jwtSecretKey);
        String token = jwtEndPointAccessTokenGenerator.createAccessToken(user.getEmail(), Collections.singletonList(UserRole.ROLE_USER.name()));
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, token);
        SecurityContextHolder.clearContext();
    }

    @Test
    void test_filter_continuousFilter() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);

        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);
        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChainSpy);
        verify(mockFilterChainSpy, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    void test_filter_loadsUserFromToken() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);

        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChain);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockUserDetailsService, times(1)).loadUserByUsername(captor.capture());
        assertThat(captor.getValue()).isEqualTo("john@email.com");
    }

    @Test
    void test_filter_setsAuthenticationInSecurityContext() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);
        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        assertThat(principal.getName()).isEqualTo("john");
        assertThat(principal.getEmail()).isEqualTo("john@email.com");
        assertThat(principal.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
        assertThat(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
    }

    @Test
    void test_filter_continuousToNextFilter_whenRequestHasNoToken() throws ServletException, IOException {
        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);
        MockHttpServletRequest noTokenRequest = new MockHttpServletRequest();
        filterToTest.doFilter(noTokenRequest, mockResponse, mockFilterChainSpy);

        verify(mockFilterChainSpy, times(1)).doFilter(noTokenRequest, mockResponse);
    }
}
