package com.example.demo_tdd_security.order.domain;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.example.demo_tdd_security.share.json.JsonUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {

    private String id;
    private Timestamp creationTimestamp;
    private User user;
    private Integer price;
    private List<String> items;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.items = new ArrayList<>();
    }

    @Builder
    public Order(Timestamp creationTimestamp, User user, Integer price) {
        this();
        this.creationTimestamp = creationTimestamp;
        this.user = user;
        this.price = price;
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "user" :
                    this.user = JsonUtils.fromJson(value, User.class);
                    break;
                case "price":
                    this.price = Integer.valueOf(value);
                    break;
                default:
                    throw new IllegalArgumentException("No such fields : " + name);
            }
        }
    }
}
