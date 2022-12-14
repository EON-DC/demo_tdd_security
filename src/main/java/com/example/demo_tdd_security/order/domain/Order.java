package com.example.demo_tdd_security.order.domain;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.example.demo_tdd_security.share.json.JsonUtil;
import com.example.demo_tdd_security.shipping.domain.ShippingAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {

    private String id;
    private Timestamp creationTimestamp;
    private Integer price;
    private User user;
    private List<Item> items;
    private ShippingAddress shippingAddress;


    public Order() {
        id = UUID.randomUUID().toString();
        items = new ArrayList<>();
        creationTimestamp = new Timestamp(new Date().getTime());
    }

    @Builder
    public Order(Integer price, User user, ShippingAddress shippingAddress) {
        this();
        this.price = price;
        this.user = user;
        this.shippingAddress = shippingAddress;
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "price":
                    this.price = Integer.valueOf(value);
                    break;
                case "user":
                    this.user = JsonUtil.fromJson(value, User.class);
                    break;
                case "shippingAddress":
                    this.shippingAddress = JsonUtil.fromJson(value, ShippingAddress.class);
                    break;
                case "items":
                    this.items = JsonUtil.fromJsonList(value, Item.class);
                    break;
                default:
                    throw new RuntimeException("No such field : " + name);
            }
        }
    }


}
