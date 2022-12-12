package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User get(String id);

    User add(User user);


    User update(String id, NameValueList nameValueList);

    void delete(String id);
}
