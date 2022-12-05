package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOrder(String id);

    Order updateOrder(String id, NameValueList nameValueList);

    void deleteOrder(String id);

    Order addOrder(Order order);
}
