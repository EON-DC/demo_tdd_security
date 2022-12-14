package com.example.demo_tdd_security.item.rest;

import com.example.demo_tdd_security.item.domain.Item;
import com.example.demo_tdd_security.item.store.ItemEntity;
import com.example.demo_tdd_security.item.store.ItemJpaStore;
import com.example.demo_tdd_security.item.store.ItemStore;
import com.example.demo_tdd_security.share.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTests {

    private ItemStore mockItemStore;
    private Item item;
    private ItemEntity itemEntity;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .name("Book")
                .stockQuantity(3)
                .build();

        itemEntity = new ItemEntity(item);

        mockItemStore = mock(ItemJpaStore.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ItemController(mockItemStore))
                .build();
    }

    @Test
    void tdd_for_getAll() throws Exception {
        // given
        when(mockItemStore.getAll()).thenReturn(
                Arrays.asList(new Item[]{
                        item, new Item("coke", 3)
                })
        );
        // when
        String contentAsString = mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        List<Item> items = JsonUtil.fromJsonList(contentAsString, Item.class);

        // then
        assertThat(items.size()).isEqualTo(2);
        List<String> itemNames = items.stream().map(Item::getName).collect(Collectors.toList());
        assertThat(itemNames.contains("Book")).isTrue();
        assertThat(itemNames.contains("coke")).isTrue();
    }

    @Test
    void tdd_for_getItem_returnsItem() throws Exception {
        // given
        when(mockItemStore.get(anyString()))
                .thenReturn(item);

        // when
        String contentAsString = mockMvc.perform(get("/items/1"))
                .andReturn().getResponse().getContentAsString();

        Item findItem = JsonUtil.fromJson(contentAsString, Item.class);

        // then
        assertThat(findItem.getName()).isEqualTo("Book");
    }

    @Test
    void tdd_for_addItem_returnsItem() throws Exception {
        // given
        when(mockItemStore.add(any()))
                .thenReturn(item);

        // when
        String contentAsString = mockMvc.perform(post("/items")
                .content(JsonUtil.toJson(item))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();

        Item addedItem = JsonUtil.fromJson(contentAsString, Item.class);


        // then
        assertThat(addedItem.getName()).isEqualTo("Book");
    }

    @Test
    void tdd_for_updateItem_returnsItem() throws Exception {
        // given
        when(mockItemStore.update(any()))
                .thenReturn(item);

        // when
        String contentAsString = mockMvc.perform(patch("/items/1")
                .content(JsonUtil.toJson(item))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();

        Item updatedItem = JsonUtil.fromJson(contentAsString, Item.class);


        // then
        assertThat(updatedItem.getName()).isEqualTo("Book");
    }

    @Test
    void tdd_for_deleteItem_deleteItem() throws Exception {
        // when
        mockMvc.perform(delete("/items/1")
        ).andExpect(status().isNoContent());

    }


}
