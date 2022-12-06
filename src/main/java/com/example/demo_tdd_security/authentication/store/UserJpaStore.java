package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserJpaStore implements UserStore {

    private UserJpaRepository jpaRepository;

    public UserJpaStore(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> userEntities = jpaRepository.findAll();
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(userEntity.toDomain());
        }
        return users;
    }

    @Override
    public User getUser(String id) {
        UserEntity userEntity = jpaRepository.findById(id).orElseThrow(
                () -> new NoSuchUserException("No such user id : " + id)
        );
        return userEntity.toDomain();
    }

    @Override
    public User saveUser(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public User updateUser(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public void deleteUser(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return jpaRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid username or password"))
                .toDomain();
    }
}
