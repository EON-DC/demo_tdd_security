package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "TB_USER")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles;

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true)
    private List<OrderEntity> orders;

    public UserEntity() {
        this.id = UUID.randomUUID().toString();
        this.orders = new ArrayList<>();
    }

    public UserEntity(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User toDomain(){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        if (orders.size() > 0) {
            for (OrderEntity orderEntity : orders) {
                Order order = new Order();
                BeanUtils.copyProperties(orderEntity, order);
                user.getOrders().add(order);
            }
        }
        return user;
    }
}
