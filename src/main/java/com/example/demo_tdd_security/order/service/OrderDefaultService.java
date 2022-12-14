package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderJpaStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderDefaultService implements OrderService {

    private final OrderJpaStore orderJpaStore;

    public OrderDefaultService(OrderJpaStore orderJpaStore) {
        this.orderJpaStore = orderJpaStore;
    }

    @Override
    public List<Order> getAll() {
        return orderJpaStore.getAllOrders();
    }

    @Override
    public Order getOrder(String id) {
        return orderJpaStore.getOrder(id);
    }

    @Override
    public Order addOrder(Order order) {
        return orderJpaStore.addOrder(order);
    }

    @Override
    public Order updateOrder(String id, NameValueList nameValueList) {
        Order order = orderJpaStore.getOrder(id);
        order.setValues(nameValueList);
        return orderJpaStore.addOrder(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderJpaStore.deleteOrder(id);
    }
}
