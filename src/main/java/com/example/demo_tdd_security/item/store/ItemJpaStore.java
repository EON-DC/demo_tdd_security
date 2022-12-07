package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;
import org.springframework.stereotype.Repository;

@Repository
public class ItemJpaStore implements ItemStore {

    private final ItemJpaRepository jpaRepository;

    public ItemJpaStore(ItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Item add(Item item) {
        return jpaRepository.save(new ItemEntity(item)).toDomain();
    }
}
