package com.example.demo_tdd_security.shipping.store;

import com.example.demo_tdd_security.shipping.domain.ShippingAddress;

import java.util.List;

public interface ShippingStore {

    List<ShippingAddress> getAll();

    ShippingAddress get(String id);

    ShippingAddress update(ShippingAddress shippingAddress);

    ShippingAddress add(ShippingAddress shippingAddress);

    void delete(String id);
}
