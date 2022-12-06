package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(String id);

    User addUser(User user);

    User updateUser(String id, NameValueList nameValueList);

    void deleteUser(String id);
}
