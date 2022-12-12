package com.example.demo_tdd_security.item.domain;

import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Item {

    private String id;
    private String name;
    private Integer stockQuantity;

    public Item() {
        id = UUID.randomUUID().toString();
    }

    @Builder
    public Item(String name, Integer stockQuantity) {
        this();
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "name" :
                    this.name = value;
                    break;
                case "stockQuantity" :
                    this.stockQuantity = Integer.valueOf(value);
                    break;
                default:
                    throw new RuntimeException("No such field : " + name);
            }
        }
    }
}
