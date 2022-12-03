package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemEntity {

    @Id
    private String id;
    private String name;

    public ItemEntity(Item item) {
        BeanUtils.copyProperties(item, this);
    }

    public Item toDomain(){
        Item item = new Item();
        BeanUtils.copyProperties(this, item);
        return item;
    }
}
