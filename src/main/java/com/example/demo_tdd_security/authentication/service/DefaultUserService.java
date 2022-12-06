package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import com.example.demo_tdd_security.authentication.store.UserStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserStore userStore;

    public DefaultUserService(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public List<User> getAllUsers() {
        return userStore.getAllUsers();
    }

    @Override
    public User getUser(String id) {
        return userStore.getUser(id);
    }

    @Override
    public User addUser(User user) {
        return userStore.saveUser(user);
    }

    @Override
    public User updateUser(String id, NameValueList nameValueList) {
        User user = userStore.getUser(id);
        user.setValues(nameValueList);
        return userStore.updateUser(user);
    }

    @Override
    public void deleteUser(String id) {
        userStore.getUser(id);
        userStore.deleteUser(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userStore.getUserByEmail(email);
            return user;
        } catch (Exception e) {
            throw new NoSuchUserException(e.getMessage());
        }
    }


}
