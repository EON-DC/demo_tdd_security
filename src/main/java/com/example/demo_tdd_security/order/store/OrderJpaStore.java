package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderJpaStore implements OrderStore {

    private final OrderJpaRepository jpaRepository;

    public OrderJpaStore(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Order> getAll() {
        return jpaRepository.findAll()
                .stream().map(OrderEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Order get(String id) {
        return jpaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No such order id : " + id)
        ).toDomain();
    }

    @Override
    public Order add(Order order) {
        return jpaRepository.save(new OrderEntity(order)).toDomain();
    }

    @Override
    public Order update(Order order) {
        return jpaRepository.save(new OrderEntity(order)).toDomain();

    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
