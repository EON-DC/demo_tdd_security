package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserJpaStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService {

    private final UserJpaStore userJpaStore;

    public DefaultUserService(UserJpaStore userJpaStore) {
        this.userJpaStore = userJpaStore;
    }

    @Override
    public List<User> getAllUsers() {
        return userJpaStore.getAllUsers();
    }

    @Override
    public User get(String id) {
        return userJpaStore.getUser(id);
    }

    @Override
    public User add(User user) {
        return userJpaStore.addUser(user);
    }

    @Override
    public User update(String id, NameValueList nameValueList) {
        User findUser = userJpaStore.getUser(id);
        findUser.setValues(nameValueList);

        return userJpaStore.updateUser(findUser);
    }

    @Override
    public void delete(String id) {
        userJpaStore.deleteUser(id);
    }
}
