package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOrderService implements  OrderService{

    private OrderStore orderStore;

    public DefaultOrderService(OrderStore orderStore) {
        this.orderStore = orderStore;
    }

    @Override
    public List<Order> getAll() {
        return orderStore.getAllOrders();
    }

    @Override
    public Order getOrder(String id) {
        return orderStore.getOrder(id);
    }

    @Override
    public Order updateOrder(String id, NameValueList nameValueList) {
        Order order = orderStore.getOrder(id);
        order.setValues(nameValueList);
        return orderStore.updateOrder(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderStore.getOrder(id);
        orderStore.deleteOrder(id);
    }

    @Override
    public Order addOrder(Order order) {
        return orderStore.addOrder(order);
    }
}
