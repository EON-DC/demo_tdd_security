package com.example.demo_tdd_security.shipping.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShippingAddress {

    private String id;
    private String name;
    private String address;
    private String zipcode;

    public ShippingAddress() {
        id = UUID.randomUUID().toString();
    }

    @Builder

    public ShippingAddress(String name, String address, String zipcode) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
    }
}
