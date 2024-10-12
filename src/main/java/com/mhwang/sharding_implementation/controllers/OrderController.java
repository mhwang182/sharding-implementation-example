package com.mhwang.sharding_implementation.controllers;

import com.mhwang.sharding_implementation.dto.OrderCreationDTO;
import com.mhwang.sharding_implementation.service.CustomerService;
import com.mhwang.sharding_implementation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
