package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserJpaStore implements UserStore {

    private final UserJpaRepository jpaRepository;

    public UserJpaStore(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return jpaRepository.findAll().stream().map(UserEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public User getUser(String id) {
        return jpaRepository.findById(id).orElseThrow(
                () -> new NoSuchUserException("No such user id : " + id)
        ).toDomain();
    }

    @Override
    public User addUser(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public User updateUser(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public User getUserByEmail(String email) {
        return jpaRepository.getUserEntityByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid username or password.")
        ).toDomain();
    }

    @Override
    public void deleteUser(String id) {
        jpaRepository.deleteById(id);
    }
}
