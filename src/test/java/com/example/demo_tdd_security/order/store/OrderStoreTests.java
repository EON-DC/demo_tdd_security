package com.example.demo_tdd_security.order.store;

import com.example.demo_tdd_security.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderStoreTests {

    private Order order;
    private OrderEntity orderEntity;
    private OrderJpaRepository mockJpaRepository;
    private OrderStore orderStore;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .price(1000)
                .build();

        orderEntity = new OrderEntity(order);
        mockJpaRepository = mock(OrderJpaRepository.class);
        orderStore = new OrderJpaStore(mockJpaRepository);
    }

    @Test
    void tdd_for_getAllOrders_returnListOrders() throws Exception {
        // given
        when(mockJpaRepository.findAll()).thenReturn(List.of(new OrderEntity(order)
        ));

        // when
        List<Order> allOrders = orderStore.getAllOrders();

        // then
        assertThat(allOrders.size()).isEqualTo(1);

    }

    @Test
    void tdd_for_getOrderWithWrongId_returnsException() throws Exception {
        // then
        assertThatThrownBy(() -> orderStore.getOrder("000"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void tdd_for_getOrder_returnsOrder() throws Exception {
        // given
        when(mockJpaRepository.findById(anyString()))
                .thenReturn(Optional.ofNullable(orderEntity));

        // when
        Order findOrder = orderStore.getOrder("999");

        // then
        assertThat(findOrder.getPrice()).isEqualTo(1000);
    }

    @Test
    void tdd_for_addOrder_returnsOrder() throws Exception {
        // given
        when(mockJpaRepository.save(any()))
                .thenReturn(orderEntity);

        // when
        Order savedOrder = orderStore.addOrder(order);
        // then
        assertThat(savedOrder).isInstanceOf(Order.class);
        assertThat(savedOrder.getPrice()).isEqualTo(1000);

    }

    @Test
    void tdd_for_updateOrder_returnsOrder() throws Exception {
        // given
        when(mockJpaRepository.save(any()))
                .thenReturn(orderEntity);
        // when
        Order updatedOrder = orderStore.updateOrder(order);
        // then
        assertThat(updatedOrder).isInstanceOf(Order.class);
        assertThat(updatedOrder.getPrice()).isEqualTo(1000);
    }

    @Test
    void tdd_for_deleteOrder_deletesOrder() throws Exception {
        // given

        // then
        orderStore.deleteOrder("999");
        // when
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockJpaRepository).deleteById(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("999");
    }


}
