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

    private final UserJpaStore jpaStore;

    public DefaultUserService(UserJpaStore jpaStore) {
        this.jpaStore = jpaStore;
    }

    @Override
    public List<User> getAll() {
        return jpaStore.getAll();
    }

    @Override
    public User get(String id) {
        return jpaStore.get(id);
    }

    @Override
    public User add(User user) {
        return jpaStore.add(user);
    }

    @Override
    public User update(String id, NameValueList nameValueList) {
        User user = jpaStore.get(id);
        user.setValues(nameValueList);
        return user;
    }

    @Override
    public void delete(String id) {
        jpaStore.get(id);
        jpaStore.delete(id);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return jpaStore.findByEmail(email);
    }
}
