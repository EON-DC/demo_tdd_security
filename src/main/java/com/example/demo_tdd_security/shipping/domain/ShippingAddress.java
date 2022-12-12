package com.example.demo_tdd_security.shipping.domain;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.example.demo_tdd_security.share.json.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShippingAddress {

    private String id;
    private String city;
    private String address;
    private String zipcode;

    public ShippingAddress() {
        id = UUID.randomUUID().toString();
    }

    @Builder
    public ShippingAddress(String city, String address, String zipcode) {
        this();
        this.city = city;
        this.address = address;
        this.zipcode = zipcode;
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "city" :
                    this.city = value;
                    break;
                case "address" :
                    this.address = value;
                    break;
                case "zipcode" :
                    this.zipcode = value;
                    break;
                default:
                    throw new RuntimeException("No such field : " + name);
            }
        }
    }
}
