package com.example.demo_tdd_security.order.rest;


import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.service.OrderService;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping(path = "{id}")
    public Order get(@PathVariable String id) {
        return orderService.get(id);
    }

    @PostMapping
    public Order save(@RequestBody Order order) {
        return orderService.add(order);
    }

    @PatchMapping(path = "{id}")
    public Order patch(@PathVariable String id, @RequestBody NameValueList nameValueList) {
        return orderService.patch(id, nameValueList);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }
}
