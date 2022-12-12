package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_USER")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = new ArrayList<>();
    @OneToMany(mappedBy = "userEntity", orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>();

    protected UserEntity() {
        id = UUID.randomUUID().toString();
        roles = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public UserEntity(User user){
        BeanUtils.copyProperties(user, this);
    }

    public User toDomain() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        if (orders.size() > 0) {
            for (OrderEntity entity : orders) {
                Order order = new Order();
                BeanUtils.copyProperties(entity, order);
                user.getOrders().add(order);
            }
        }
        return user;
    }





}
