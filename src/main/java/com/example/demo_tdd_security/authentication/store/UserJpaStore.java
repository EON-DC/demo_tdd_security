package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserJpaStore implements UserStore {

    private final UserJpaRepository jpaRepository;

    public UserJpaStore(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<User> getAll() {
        return jpaRepository.findAll()
                .stream().map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User get(String id) {
        return jpaRepository.findById(id).orElseThrow(
                () -> new NoSuchUserException("No such user id : " + id)).toDomain();
    }

    @Override
    public User add(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public User update(User user) {
        return jpaRepository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    public User findByEmail(String email){
        return jpaRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchUserException("Invalid Email or password...")
        ).toDomain();
    }

}
