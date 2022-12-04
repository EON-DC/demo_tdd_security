package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;

import java.util.List;

public interface UserStore {

    List<User> getAllUsers();

    User getUser(String id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(String id);

    User getUserByEmail(String email);

}
