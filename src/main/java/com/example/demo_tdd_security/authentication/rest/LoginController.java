package com.example.demo_tdd_security.authentication.rest;

import antlr.Token;
import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.rest.dto.LoginRequestDto;
import com.example.demo_tdd_security.authentication.rest.dto.TokenResponse;
import com.example.demo_tdd_security.authentication.store.UserStore;
import com.example.demo_tdd_security.share.token.EndpointAccessTokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserStore userStore;
    private final EndpointAccessTokenGenerator endpointAccessTokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserStore userStore,
                           EndpointAccessTokenGenerator endpointAccessTokenGenerator,
                           PasswordEncoder passwordEncoder) {
        this.userStore = userStore;
        this.endpointAccessTokenGenerator = endpointAccessTokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody  LoginRequestDto loginRequestDto) {
        User user = userStore.getUserByEmail(loginRequestDto.getEmail());
        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()) == false) {
            throw new RuntimeException("login failed.. Invalid username or password");
        }
        return TokenResponse.builder()
                .accessToken(endpointAccessTokenGenerator.createAccessToken(loginRequestDto.getEmail(), user.getRolesAsString()))
                .refreshToken(endpointAccessTokenGenerator.createRefreshToken(loginRequestDto.getEmail(), user.getRolesAsString()))
                .build();
    }
}
