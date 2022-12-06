package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderJpaStore;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOrderService implements OrderService{

    private final OrderJpaStore jpaStore;

    public DefaultOrderService(OrderJpaStore jpaStore) {
        this.jpaStore = jpaStore;
    }

    @Override
    public List<Order> getAllOrders() {
        return jpaStore.getAllOrders();
    }

    @Override
    public Order getOrder(String id) {
        return jpaStore.getOrder(id);
    }

    @Override
    public Order addOrder(Order order) {
        return jpaStore.addOrder(order);
    }

    @Override
    public Order updateOrder(String id, NameValueList nameValueList) {
        Order order = jpaStore.getOrder(id);
        order.setValues(nameValueList);
        return jpaStore.updateOrder(order);
    }

    @Override
    public void deleteOrder(String id) {
        jpaStore.getOrder(id);
        jpaStore.deleteOrder(id);
    }
}
