package com.example.demo_tdd_security.authentication.rest;


import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.rest.dto.SignupRequestDto;
import com.example.demo_tdd_security.authentication.store.UserStore;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class SignupController {

    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserStore userStore, PasswordEncoder passwordEncoder) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto) {
        User savedUser = userStore.addUser(User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build());
        return savedUser.getId();
    }
}
