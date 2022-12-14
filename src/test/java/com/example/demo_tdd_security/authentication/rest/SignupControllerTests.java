package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.rest.dto.LoginDto;
import com.example.demo_tdd_security.share.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SignupControllerTests {

    private LoginDto loginDto;
    private MockMvc mockMvc;
    private UserSpyStubStore userSpyStubStore;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto("john@email.com", "password");
        userSpyStubStore = new UserSpyStubStore();
        passwordEncoder = new BCryptPasswordEncoder();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new SignupController(userSpyStubStore, passwordEncoder))
                .build();
    }

    @Test
    void tdd_for_signUp_returnsOk() throws Exception{
        // then
        String contentAsString = mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        assertThat(contentAsString).isEqualTo("Ok");
    }


    @Test
    void tdd_for_signUp_usesCorrectLoginDto() throws Exception{
        // when
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(loginDto)));

        // then
        User addUser_argument_body = userSpyStubStore.getAddUser_argument_body();
        assertThat(addUser_argument_body.getName()).isNull();
        assertThat(addUser_argument_body.getEmail()).isEqualTo("john@email.com");
        String password = addUser_argument_body.getPassword();
        assertThat(password).isNotNull();
        assertThat(passwordEncoder.matches("password", password)).isTrue();
    }



}
