package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.rest.dto.LoginDto;
import com.example.demo_tdd_security.authentication.rest.dto.SignupDto;
import com.example.demo_tdd_security.authentication.store.UserStore;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserStore userStore, PasswordEncoder passwordEncoder) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String signup(@RequestBody SignupDto signupDto) {
        User user = User.builder().email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build();
        userStore.addUser(user);
        return "Ok";
    }
}
