package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserStore;

import java.util.List;

public class UserSpyStubStore implements UserStore {

    private String getUser_argument_id;
    private User addUser_argument_body;
    private User updateUser_argument_body;
    private String getUserByEmail_argument_email;
    private String deleteUser_argument_id;

    private List<User> getAllUsers_returnValue;
    private User getUser_returnValue;
    private User addUser_returnValue;
    private User updateUser_returnValue;
    private User getUserByEmail_returnValue;

    @Override
    public List<User> getAllUsers() {
        return getAllUsers_returnValue;
    }

    @Override
    public User getUser(String id) {
        getUser_argument_id = id;
        return getUser_returnValue;
    }

    @Override
    public User addUser(User user) {
        addUser_argument_body = user;
        return addUser_returnValue;
    }

    @Override
    public User updateUser(User user) {
        updateUser_argument_body = user;
        return updateUser_returnValue;
    }

    @Override
    public User getUserByEmail(String email) {
        getUserByEmail_argument_email = email;
        return getUserByEmail_returnValue;
    }

    @Override
    public void deleteUser(String id) {
        deleteUser_argument_id = id;

    }

    public void setGetAllUsers_returnValue(List<User> getAllUsers_returnValue) {
        this.getAllUsers_returnValue = getAllUsers_returnValue;
    }

    public void setGetUser_returnValue(User getUser_returnValue) {
        this.getUser_returnValue = getUser_returnValue;
    }

    public void setAddUser_returnValue(User addUser_returnValue) {
        this.addUser_returnValue = addUser_returnValue;
    }

    public void setUpdateUser_returnValue(User updateUser_returnValue) {
        this.updateUser_returnValue = updateUser_returnValue;
    }

    public void setGetUserByEmail_returnValue(User getUserByEmail_returnValue) {
        this.getUserByEmail_returnValue = getUserByEmail_returnValue;
    }

    public String getGetUser_argument_id() {
        return getUser_argument_id;
    }

    public User getAddUser_argument_body() {
        return addUser_argument_body;
    }

    public User getUpdateUser_argument_body() {
        return updateUser_argument_body;
    }

    public String getGetUserByEmail_argument_email() {
        return getUserByEmail_argument_email;
    }

    public String getDeleteUser_argument_id() {
        return deleteUser_argument_id;
    }
}
