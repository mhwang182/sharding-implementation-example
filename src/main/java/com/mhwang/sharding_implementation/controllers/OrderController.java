package com.mhwang.sharding_implementation.controllers;

import com.mhwang.sharding_implementation.dto.OrderCreationDTO;
import com.mhwang.sharding_implementation.repository.model.Order;
import com.mhwang.sharding_implementation.service.CustomerService;
import com.mhwang.sharding_implementation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreationDTO order) {

        orderService.createOrderAndAddToCustomer(order.getCustomerId(), order.getProductSku());

        return ResponseEntity.ok().body("Order successfully created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {

        Optional<Order> orderOpt = orderService.getOrder(id);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        return ResponseEntity.ok().body(orderOpt.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody Order order) {

        Order updatedOrder = orderService.updateOrder(id, order);

        if (updatedOrder == null) {
            return ResponseEntity.status(404).body("Cannot update order");
        }

        return ResponseEntity.ok().body("Order successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {

        orderService.deleteOrder(id);

        return ResponseEntity.ok().body("Order Deleted");
    }
}
