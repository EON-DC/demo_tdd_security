package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.rest.dto.LoginRequestDto;
import com.example.demo_tdd_security.authentication.rest.dto.TokenResponse;
import com.example.demo_tdd_security.authentication.store.UserStore;
import com.example.demo_tdd_security.share.jwt.EndPointAccessTokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserStore userStore;
    private final EndPointAccessTokenGenerator endPointAccessTokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserStore userStore,
                           EndPointAccessTokenGenerator endPointAccessTokenGenerator,
                           PasswordEncoder passwordEncoder) {
        this.userStore = userStore;

        this.endPointAccessTokenGenerator = endPointAccessTokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userStore.getUserByEmail(loginRequestDto.getEmail());
        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()) == false) {
            throw new RuntimeException("invalid username or password");
        }
        String accessToken = endPointAccessTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString());
        String refreshToken = endPointAccessTokenGenerator.createRefreshToken(user.getEmail(), user.getRolesAsString());
        TokenResponse token = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return token;
    }
}
