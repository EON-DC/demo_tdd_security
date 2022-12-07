package com.example.demo_tdd_security.item.rest;

import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.item.store.ItemStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemRestController {

    private final ItemStore itemStore;

    public ItemRestController(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    @PostMapping("/items")
    public Item saveItem(Item item) {
        return itemStore.add(item);
    }
}
