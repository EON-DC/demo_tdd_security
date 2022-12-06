package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class ItemJpaStore implements ItemStore {

    private final ItemJpaRepository jpaRepository;

    public ItemJpaStore(ItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Item> getAll() {
        return jpaRepository.findAll().stream()
                .map(ItemEntity::toDomain)
                .collect(Collectors.toList());
    }
}
