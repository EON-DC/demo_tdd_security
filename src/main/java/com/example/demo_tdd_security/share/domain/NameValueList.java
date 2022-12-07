package com.example.demo_tdd_security.share.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class NameValueList {

    private List<NameValue> nameValues;

    public NameValueList(List<NameValue> nameValues) {
        this.nameValues = nameValues;
    }

    public void add(NameValue nameValue) {
        this.nameValues.add(nameValue);
    }
}
