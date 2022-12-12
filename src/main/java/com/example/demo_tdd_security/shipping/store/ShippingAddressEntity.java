package com.example.demo_tdd_security.shipping.store;

import com.example.demo_tdd_security.shipping.domain.ShippingAddress;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "TB_SHIPPING")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingAddressEntity {

    @Id
    @Column(name = "shipping_id")
    private String id;
    private String city;
    private String address;
    private String zipcode;

    @Builder
    public ShippingAddressEntity(ShippingAddress shippingAddress) {
        BeanUtils.copyProperties(shippingAddress, this);
    }

    public ShippingAddress toDomain() {
        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtils.copyProperties(this, shippingAddress);
        return shippingAddress;
    }
}
