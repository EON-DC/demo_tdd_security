package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.rest.dto.SignupRequestDto;
import com.example.demo_tdd_security.authentication.store.UserStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignupControllerTests {

    private ObjectMapper mapper;
    private SignupRequestDto requestDto;
    private PasswordEncoder passwordEncoder;
    private UserStore SpyStubUserStore;
}
