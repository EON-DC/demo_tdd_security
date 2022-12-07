package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDefaultService implements OrderService {

    private final OrderStore orderStore;

    public OrderDefaultService(OrderStore orderStore) {
        this.orderStore = orderStore;
    }

    @Override
    public List<Order> getAll() {
        return orderStore.getAll();
    }

    @Override
    public Order get(String id) {
        return orderStore.get(id);
    }

    @Override
    public Order add(Order order) {
        return orderStore.add(order);
    }

    @Override
    public Order patch(String id, NameValueList nameValueList) {
        Order order = orderStore.get(id);
        order.setValues(nameValueList);
        return orderStore.update(order);
    }

    @Override
    public void delete(String id) {
        orderStore.get(id);
        orderStore.delete(id);
    }
}
