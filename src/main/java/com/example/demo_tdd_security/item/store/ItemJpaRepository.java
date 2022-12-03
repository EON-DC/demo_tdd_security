package com.example.demo_tdd_security.item.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, String> {
}
