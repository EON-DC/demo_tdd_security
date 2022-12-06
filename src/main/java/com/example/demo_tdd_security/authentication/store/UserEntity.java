package com.example.demo_tdd_security.authentication.store;


import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Getter
@Setter
@Table(name = "TB_USER")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;

    private String name;
    private String password;
    private String phone;
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles;

    public UserEntity(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public UserEntity(){
        this.id = UUID.randomUUID().toString();
        this.roles = new ArrayList<>();
    }

    public User toDomain(){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        if (this.orders.size() > 0) {
            for (OrderEntity orderEntity : this.orders) {
                Order order = new Order();
                BeanUtils.copyProperties(orderEntity, order);
                user.getOrders().add(order);
            }
        }
        return user;
    }

}
