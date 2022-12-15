package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserEntity;
import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.item.store.ItemEntity;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.shipping.store.ShippingAddressEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TB_ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @Column(name = "order_id")
    private String id;
    private Timestamp creationTimestamp;
    private Integer price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemEntity> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "shipping_id")
    private ShippingAddressEntity shippingAddressEntity;

    public OrderEntity(Order order) {
        BeanUtils.copyProperties(order, this);
        if (order.getUser() != null) {
            userEntity = new UserEntity(order.getUser());
        }
        if (order.getItems().size() > 0) {
            for (Item item : order.getItems()) {
                items.add(new ItemEntity(item));
            }
        }
        if (order.getShippingAddress() != null) {
            shippingAddressEntity = new ShippingAddressEntity(order.getShippingAddress());
        }
    }

    public Order toDomain() {
        Order order = new Order();
        BeanUtils.copyProperties(this, order);
        if (userEntity != null) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            order.setUser(user);
        }
        if (items.size() > 0) {
            List<Item> orderItems = order.getItems();
            for (ItemEntity itemEntity : items) {
                orderItems.add(itemEntity.toDomain());
            }
        }
        if (shippingAddressEntity != null) {
            order.setShippingAddress(shippingAddressEntity.toDomain());
        }
        return order;
    }

}
