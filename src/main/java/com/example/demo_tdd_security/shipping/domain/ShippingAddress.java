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
    private String zipCode;

    public ShippingAddress() {
        this.id = UUID.randomUUID().toString();
    }

    @Builder
    public ShippingAddress(String name, String address, String zipCode) {
        this();
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
    }
}
