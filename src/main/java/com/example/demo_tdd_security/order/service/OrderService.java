package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.share.domain.NameValueList;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order get(String id);

    Order add(Order order);

    Order patch(String id, NameValueList nameValueList);

    void delete(String id);
}
