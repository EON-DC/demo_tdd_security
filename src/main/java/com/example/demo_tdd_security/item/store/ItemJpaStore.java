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
                .map(ItemEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Item get(String id) {
        return jpaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No Such Id : " + id)
        ).toDomain();
    }

    @Override
    public Item add(Item item) {
        return jpaRepository.save(new ItemEntity(item)).toDomain();
    }

    @Override
    public Item update(Item item) {
        return jpaRepository.save(new ItemEntity(item)).toDomain();
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
