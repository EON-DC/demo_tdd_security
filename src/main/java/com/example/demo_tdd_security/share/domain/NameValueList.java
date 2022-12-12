package com.example.demo_tdd_security.share.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NameValueList {

    private List<NameValue> nameValues = new ArrayList<>();

    public NameValueList() {
        nameValues = new ArrayList<>();
    }

    public NameValueList(List<NameValue> nameValues) {
        this.nameValues = nameValues;
    }

    public void add(NameValue nameValue) {
        nameValues.add(nameValue);
    }
}
