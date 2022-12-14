package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.rest.dto.LoginDto;
import com.example.demo_tdd_security.authentication.store.UserStore;
import com.example.demo_tdd_security.share.jwt.JwtEndpointAccessTokenGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator;

    public LoginController(UserStore userStore,
                           PasswordEncoder passwordEncoder,
                           JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtEndpointAccessTokenGenerator = jwtEndpointAccessTokenGenerator;
    }

    @PostMapping
    public String login(@RequestBody LoginDto loginDto) {
        User user = userStore.getUserByEmail(loginDto.getEmail());
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return "AccessToken : " + jwtEndpointAccessTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString())
                    + " RefreshToken : " + jwtEndpointAccessTokenGenerator.createRefreshToken(user.getEmail(), user.getRolesAsString());
        }
        return "Invalid username or password";
    }
}
