package com.example.demo_tdd_security.item.rest;

import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.item.store.ItemStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemStore itemStore;

    public ItemController(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemStore.getAll();}


}
