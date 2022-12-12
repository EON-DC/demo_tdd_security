package com.example.demo_tdd_security.authentication.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> getUserEntityByEmail(String email);
}
