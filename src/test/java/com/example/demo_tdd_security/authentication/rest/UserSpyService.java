package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.service.UserService;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public class UserSpyService implements UserService {

    private String get_argument_id;
    private User add_argument_body;
    private String update_argument_id;
    private NameValueList update_argument_body;
    private String delete_argument_id;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User get(String id) {
        get_argument_id = id;
        return null;
    }

    @Override
    public User add(User user) {
        add_argument_body = user;
        return null;
    }

    @Override
    public User update(String id, NameValueList nameValueList) {
        update_argument_id = id;
        update_argument_body = nameValueList;
        return null;
    }

    @Override
    public void delete(String id) {
        delete_argument_id = id;
    }

    public String getGet_argument_id() {
        return get_argument_id;
    }

    public User getAdd_argument_body() {
        return add_argument_body;
    }

    public String getUpdate_argument_id() {
        return update_argument_id;
    }

    public NameValueList getUpdate_argument_body() {
        return update_argument_body;
    }

    public String getDelete_argument_id() {
        return delete_argument_id;
    }
}
