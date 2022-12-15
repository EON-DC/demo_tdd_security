package com.example.demo_tdd_security.shipping.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressJpaRepository extends JpaRepository<ShippingAddressEntity, String> {
}
