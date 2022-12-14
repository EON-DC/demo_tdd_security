package com.example.demo_tdd_security.item.rest;

import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.item.store.ItemStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemStore itemStore;

    public ItemController(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    @GetMapping
    public List<Item> getAll() {
        return itemStore.getAll();
    }

    @GetMapping(path = "{id}")
    public Item get(@PathVariable String id) {
        return itemStore.get(id);
    }

    @PostMapping
    public Item add(@RequestBody Item item) {
        return itemStore.add(item);
    }

    @PatchMapping(path = "{id}")
    public Item update(@PathVariable String id, @RequestBody Item item) {
        return itemStore.update(item);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        itemStore.delete(id);
    }
}
