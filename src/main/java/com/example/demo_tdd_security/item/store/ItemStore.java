package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;

import java.util.List;

public interface ItemStore {

    List<Item> getAll();

    Item get(String id);

    Item add(Item item);

    Item update(Item item);

    void delete(String id);
}
