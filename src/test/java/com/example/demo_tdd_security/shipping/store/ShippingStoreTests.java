package com.example.demo_tdd_security.shipping.store;

import com.example.demo_tdd_security.shipping.domain.ShippingAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ShippingStoreTests {

    @Autowired
    private ShippingAddressJpaRepository shippingJpaRepository;

    private ShippingStore shippingStore;
    private ShippingAddress homeAddress;

    @BeforeEach
    void setUp() {
        shippingStore = new ShippingJpaStore(shippingJpaRepository);
        homeAddress = ShippingAddress.builder()
                .name("home")
                .address("17 street")
                .zipcode("1234").build();
    }

    @Test
    void test_getAll_returnsList(){
        shippingJpaRepository.saveAll(
                Arrays.asList(new ShippingAddressEntity[]{
                        new ShippingAddressEntity(new ShippingAddress()),
                        new ShippingAddressEntity(new ShippingAddress())
                })
        );

        List<ShippingAddress> addresses = shippingStore.getAll();
        assertThat(addresses).size().isEqualTo(2);
    }

    @Test
    void test_getShippingAddress_returnShippingAddress() {

    }
}
