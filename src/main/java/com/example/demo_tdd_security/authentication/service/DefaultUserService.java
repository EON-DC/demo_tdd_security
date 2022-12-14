package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserJpaStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService, UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User userByEmail = userJpaStore.getUserByEmail(email);
            return userByEmail;
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
