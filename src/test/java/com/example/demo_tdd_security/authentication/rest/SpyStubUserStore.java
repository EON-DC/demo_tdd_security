package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserStore;

import java.util.List;

public class SpyStubUserStore implements UserStore {

    private String get_argument_id;
    private User add_argument_body;
    private User update_argument_body;
    private String delete_argument_id;
    private String getUserByEmail_argument_email;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User get(String id) {
        return null;
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
