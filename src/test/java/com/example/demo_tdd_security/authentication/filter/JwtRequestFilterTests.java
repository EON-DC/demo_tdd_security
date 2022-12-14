package com.example.demo_tdd_security.authentication.filter;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.service.DefaultUserService;
import com.example.demo_tdd_security.share.jwt.EndpointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtEndpointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTests {

    private BCryptPasswordEncoder passwordEncoder;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;
    private MockFilterChain mockFilterChain;
    private User john;
    private UserDetailsService mockUserDetailsService;

    private JwtSecretKey jwtSecretKey;
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        mockHttpServletRequest = new MockHttpServletRequest();

        mockHttpServletResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();
        john = User.builder()
                .name("john")
                .email("john@email.com")
                .password("1234")
                .roles(Arrays.asList(UserRole.ROLE_USER, UserRole.ROLE_ADMIN))
                .phone("1234-1234")
                .build();
        mockUserDetailsService = mock(DefaultUserService.class);
        jwtSecretKey = new JwtSecretKey("secretKey");

        EndpointAccessTokenGenerator tokenGenerator = new JwtEndpointAccessTokenGenerator(jwtSecretKey);
        String accessToken = tokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());

        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        jwtRequestFilter = new JwtRequestFilter(jwtSecretKey);
    }

    @Test
    void tdd_for_continuousToNextFilter() throws Exception {
        // given
        when(mockUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(john);
        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);
        // when
        jwtRequestFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChainSpy);


        // then
        verify(mockFilterChainSpy, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }


    @Test
    void tdd_for_setsAuthenticationSecurityContext() throws Exception {
        // given


        // when
        jwtRequestFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        // then
        assertThat(principal).isEqualTo(john.getEmail());
    }

    @Test
    void tdd_for_continuousToNextFilter_whenRequestHasNoToken() throws Exception {
        // given
        mockHttpServletRequest = new MockHttpServletRequest();
        MockFilterChain mockFilterChainSpy = spy(mockFilterChain);
        // when
        jwtRequestFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChainSpy);

        // then
        verify(mockFilterChainSpy, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }


}
