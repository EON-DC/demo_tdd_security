package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.order.domain.Order;

import java.util.List;

public interface OrderStore {

    List<Order> getAllOrders();

    Order getOrder(String id);

    Order addOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(String id);
}
