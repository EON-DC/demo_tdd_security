package com.example.demo_tdd_security.authentication.rest;


import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.service.UserService;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping(path = "{id}")
    public User get(@PathVariable String id) {
        return userService.get(id);
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    @PatchMapping(path = "{id}")
    public User update(@PathVariable String id, @RequestBody NameValueList nameValueList) {
        return userService.update(id, nameValueList);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
