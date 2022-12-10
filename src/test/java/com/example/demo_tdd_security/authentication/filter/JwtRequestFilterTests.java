package com.example.demo_tdd_security.authentication.filter;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.service.DefaultUserService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTests {

    private BCryptPasswordEncoder passwordEncoder;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private MockFilterChain mockFilterChain;

    private User user;
    private UserDetailsService mockUserDetailsService;

    private JwtSecretKey jwtSecretKey;
    private JwtRequestFilter filterToTest;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();

        user = User.builder()
                .email("user@email.com")
                .password(passwordEncoder.encode("password"))
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build();

        mockUserDetailsService = mock(DefaultUserService.class);
        jwtSecretKey = new JwtSecretKey("magicSecret");
        JwtEndPointAccessTokenGenerator tokenGenerator = new JwtEndPointAccessTokenGenerator(jwtSecretKey);
        String token = tokenGenerator.createAccessToken("user@email.com", Collections.singletonList(UserRole.ROLE_USER.name()));
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, token);
        filterToTest = new JwtRequestFilter(mockUserDetailsService, jwtSecretKey);
        SecurityContextHolder.clearContext();
    }

    @Test
    void test_filter_continuesToNextFilter() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);
        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);

        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChainSpy);

    }

    @Test
    void test_filter_loadsUserFromToken() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);
        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChain);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockUserDetailsService, times(1)).loadUserByUsername(captor.capture());
        assertThat(captor.getValue()).isEqualTo("user@email.com");
    }

    @Test
    void test_filter_setAuthenticationInSecurityContext() throws ServletException, IOException {
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(user);

        filterToTest.doFilter(mockRequest, mockResponse, mockFilterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        assertThat(principal.getEmail()).isEqualTo("user@email.com");
        assertThat(principal.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
        assertThat(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
    }

    @Test
    void test_filter_continuesToNextFilter_whenRequestHasNoToken() throws ServletException, IOException {
        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        filterToTest.doFilter(mockHttpServletRequest, mockResponse, mockFilterChainSpy);
        verify(mockFilterChainSpy, times(1)).doFilter(mockHttpServletRequest, mockResponse);
    }
}
