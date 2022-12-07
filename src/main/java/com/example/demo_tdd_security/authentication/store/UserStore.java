package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;

import java.util.List;

public interface UserStore {

    List<User> getAll();

    User get(String id);

    User add(User user);

    User update(User user);

    void delete(String id);


}
