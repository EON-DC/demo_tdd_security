package com.example.demo_tdd_security.item.store;

import com.example.demo_tdd_security.item.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemStoreTests {

    private Item item;

    private ItemEntity itemEntity;

    private ItemJpaRepository mockItemJpaRepository;

    private ItemJpaStore itemJpaStore;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .name("Book")
                .stockQuantity(3)
                .build();

        itemEntity = new ItemEntity(item);
        mockItemJpaRepository = mock(ItemJpaRepository.class);
        itemJpaStore = new ItemJpaStore(mockItemJpaRepository);
    }

    @Test
    void tdd_for_getAllItems_returnsListItems() throws Exception {
        // given
        when(mockItemJpaRepository.findAll()).thenReturn(
                Arrays.asList(new ItemEntity[]{
                        new ItemEntity(new Item("coke", 1)),
                        new ItemEntity(new Item("juice", 5))
                })
        );

        // when
        List<Item> all = itemJpaStore.getAll();

        // then
        assertThat(all.size()).isEqualTo(2);
        List<String> names = all.stream().map(Item::getName).collect(Collectors.toList());
        assertThat(names.contains("coke")).isTrue();
        assertThat(names.contains("juice")).isTrue();
        assertThat(names.contains("cidar")).isFalse();
    }

    @Test
    void tdd_for_getItem_returnsItem() throws Exception {
        // given
        when(mockItemJpaRepository.findById(anyString()))
                .thenReturn(Optional.ofNullable(itemEntity));

        // when
        Item findItem = itemJpaStore.get(item.getId());

        // then
        assertThat(findItem.getName()).isEqualTo("Book");
    }

    @Test
    void tdd_for_getItem_usesCorrectArgument() throws Exception {
        // given
        when(mockItemJpaRepository.findById(anyString()))
                .thenReturn(Optional.ofNullable(itemEntity));

        // when
        Item findItem = itemJpaStore.get(item.getId());

        // then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockItemJpaRepository).findById(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(item.getId());
    }

    @Test
    void tdd_for_updateItem_returnsItem() throws Exception {
        // given
        when(mockItemJpaRepository.save(any()))
                .thenReturn(itemEntity);
        // when
        Item updatedItem = itemJpaStore.update(item);
        // then
        assertThat(updatedItem.getName()).isEqualTo("Book");
        assertThat(updatedItem.getStockQuantity()).isEqualTo(3);
    }

    @Test
    void tdd_for_saveItem_returnsItem() throws Exception {
        // given
        when(mockItemJpaRepository.save(any()))
                .thenReturn(itemEntity);

        // when
        Item addedItem = itemJpaStore.add(item);

        // then
        verify(mockItemJpaRepository).save(any());
    }

    @Test
    void tdd_for_deleteItem_deletesItem() throws Exception {
        // when
        itemJpaStore.delete(item.getId());

        // then
        verify(mockItemJpaRepository).deleteById(item.getId());
    }


}
