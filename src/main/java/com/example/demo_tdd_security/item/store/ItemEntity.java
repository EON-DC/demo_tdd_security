package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

    @Id
    @Column(name = "item_id")
    private String id;
    private String name;


    public ItemEntity(Item item) {
        BeanUtils.copyProperties(item, this);
    }


    public Item toDomain() {
        Item item = new Item();
        BeanUtils.copyProperties(this, item);
        return item;
    }
}
