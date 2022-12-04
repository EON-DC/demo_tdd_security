package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

}
