package com.example.demo_tdd_security.order.rest;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.service.OrderService;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin(value = "*")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/orders")
    public Order addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @PatchMapping("/orders/{id}")
    public Order updateOrder(@PathVariable String id, @RequestBody NameValueList nameValueList) {
        return orderService.updateOrder(id, nameValueList);
    }

    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }


}
