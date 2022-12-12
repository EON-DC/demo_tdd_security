package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.service.UserService;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public class UserStubService implements UserService {
    private List<User> getAll_returnValue;
    private User get_returnValue;
    private User add_returnValue;
    private User update_returnValue;

    @Override
    public List<User> getAll() {
        return getAll_returnValue;
    }

    @Override
    public User get(String id) {
        return get_returnValue;
    }

    @Override
    public User add(User user) {
        return add_returnValue;
    }

    @Override
    public User update(String id, NameValueList nameValueList) {
        return update_returnValue;
    }

    @Override
    public void delete(String id) {

    }

    public void setGetAll_returnValue(List<User> getAll_returnValue) {
        this.getAll_returnValue = getAll_returnValue;
    }

    public void setGet_returnValue(User get_returnValue) {
        this.get_returnValue = get_returnValue;
    }

    public void setAdd_returnValue(User add_returnValue) {
        this.add_returnValue = add_returnValue;
    }

    public void setUpdate_returnValue(User update_returnValue) {
        this.update_returnValue = update_returnValue;
    }
}
