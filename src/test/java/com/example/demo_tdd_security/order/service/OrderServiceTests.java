package com.example.demo_tdd_security.order.service;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderJpaStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceTests {

    private Order order;
    private OrderJpaStore mockOrderJpaStore;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .price(1000)
                .build();

        mockOrderJpaStore = mock(OrderJpaStore.class);
        orderService = new OrderDefaultService(mockOrderJpaStore);
    }

    @Test
    void tdd_for_getAllOrders_returnsListOrders() throws Exception {
        // given
        when(mockOrderJpaStore.getAllOrders()).thenReturn(Arrays.asList(order));

        // when
        List<Order> orders = orderService.getAll();

        // then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    void tdd_for_getOrder_returnsOrder() throws Exception{
        // given
        when(mockOrderJpaStore.getOrder(anyString())).thenReturn(order);
        // when
        Order findOrder = orderService.getOrder("999");

        // then
        assertThat(findOrder.getId()).isEqualTo(order.getId());

    }

    @Test
    void tdd_for_getOrderWithWrongId_throwsException() throws Exception{
        // given
        when(mockOrderJpaStore.getOrder(anyString()))
                .thenThrow(new RuntimeException());
        // then
        assertThatThrownBy(() -> orderService.getOrder("22"))
                .isInstanceOf(RuntimeException.class);
    }







}
