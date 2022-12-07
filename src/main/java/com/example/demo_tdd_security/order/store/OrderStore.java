package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.order.domain.Order;

import java.util.List;

public interface OrderStore {

    List<Order> getAll();

    Order get(String id);

    Order add(Order order);

    Order update(Order order);

    void delete(String id);
}
