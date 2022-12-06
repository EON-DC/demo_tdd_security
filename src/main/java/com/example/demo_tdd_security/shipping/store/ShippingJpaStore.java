package com.example.demo_tdd_security.shipping.store;

import com.example.demo_tdd_security.shipping.domain.ShippingAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShippingJpaStore  implements ShippingStore{

    private final ShippingJpaRepository jpaRepository;

    public ShippingJpaStore(ShippingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ShippingAddress> getAll() {
        return jpaRepository.findAll().stream().map(ShippingAddressEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public ShippingAddress get(String id) {
        return jpaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No such Shipping address id : " + id)
        ).toDomain();
    }

    @Override
    public ShippingAddress update(ShippingAddress shippingAddress) {
        return jpaRepository.save(new ShippingAddressEntity(shippingAddress)).toDomain();
    }

    @Override
    public ShippingAddress add(ShippingAddress shippingAddress) {
        return jpaRepository.save(new ShippingAddressEntity(shippingAddress)).toDomain();
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
