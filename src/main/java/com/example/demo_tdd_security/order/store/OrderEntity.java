package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserEntity;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.share.json.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity(name = "order")
@Getter
@Setter
@Table(name = "TB_ORDER")
public class OrderEntity {

    @Id
    private String id;

    private Timestamp creationTimestamp;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    private String items;

    public OrderEntity() {
        this.id = UUID.randomUUID().toString();
    }

    public OrderEntity(Order order) {
        BeanUtils.copyProperties(order, this);
        if (order.getUser() != null) {
            this.userEntity = new UserEntity(order.getUser());
        }
        if (order.getItems().size() > 0) {
            this.items = JsonUtil.toJson(order.getItems());
        }
    }

    public Order toDomain(){
        Order order = new Order();
        BeanUtils.copyProperties(this, order);
        if (getUserEntity() != null) {
            User user = new User();
            BeanUtils.copyProperties(this.userEntity, user);
        }
        if (getItems() != null) {
            List<String> itemFromJson = JsonUtil.fromJsonList(items, String.class);
            order.setItems(itemFromJson);
        }
        return order;
    }
}
