package com.example.demo_tdd_security.order.rest;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.service.OrderService;
import com.example.demo_tdd_security.order.store.OrderJpaStore;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(value = "*")

public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> findAll() {
        return orderService.getAllOrders();
    }

    @GetMapping(path = "{id}")
    public Order getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @PatchMapping(path = "{id}")
    public Order updateOrder(@PathVariable String id, @RequestBody NameValueList nameValueList) {
        return orderService.updateOrder(id, nameValueList);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }
}
