package com.example.demo_tdd_security.share.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

public class NameValue {

    private String name;
    private String value;

    public NameValue() {
    }

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
