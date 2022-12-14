package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.rest.dto.JwtTokenResponse;
import com.example.demo_tdd_security.authentication.rest.dto.LoginDto;
import com.example.demo_tdd_security.share.json.JsonUtil;
import com.example.demo_tdd_security.share.jwt.JwtEndpointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class LoginControllerTests {

    private User john;

    private MockMvc mockMvc;
    private LoginDto loginDto;
    private UserSpyStubStore userSpyStubStore;

    private PasswordEncoder passwordEncoder;

    private JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator;
    private JwtSecretKey jwtSecretKey;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        john = User.builder()
                .name("john")
                .email("john@email.com")
                .password(passwordEncoder.encode("1234"))
                .roles(Arrays.asList(new UserRole[]{UserRole.ROLE_USER, UserRole.ROLE_ADMIN}))
                .phone("1234-1234")
                .build();
        loginDto = new LoginDto("john@email.com", "1234");
        userSpyStubStore = new UserSpyStubStore();
        jwtSecretKey = new JwtSecretKey("secretKey");
        jwtEndpointAccessTokenGenerator = new JwtEndpointAccessTokenGenerator(jwtSecretKey);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new LoginController(userSpyStubStore,
                        passwordEncoder,
                        jwtEndpointAccessTokenGenerator))
                .build();
    }

    @Test
    void tdd_for_login_returnsOk() throws Exception{
        userSpyStubStore.setGetUserByEmail_returnValue(new User());

        // then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginDto)))
                .andExpect(status().isOk());

    }

    @Test
    void tdd_for_login_returnsCorrectJwtToken() throws Exception{
        // given
        userSpyStubStore.setGetUserByEmail_returnValue(john);

        // when
        String contentAsString = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JwtTokenResponse jwtTokenResponse = JsonUtil.fromJson(contentAsString, JwtTokenResponse.class);
        String accessToken = jwtTokenResponse.getAccessToken();
        String refreshToken = jwtTokenResponse.getRefreshToken();

        String accessTokenFromGenerator = jwtEndpointAccessTokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());
        String refreshFromGenerator = jwtEndpointAccessTokenGenerator.createRefreshToken(john.getEmail(), john.getRolesAsString());

        assertThat(accessToken).isEqualTo(accessTokenFromGenerator);
        assertThat(refreshToken).isEqualTo(refreshFromGenerator);
    }



}
