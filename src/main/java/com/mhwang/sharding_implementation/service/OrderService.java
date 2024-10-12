package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.repository.OrderRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.repository.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Long productSku) {

        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setProductSku(productSku);

        return orderRepository.save(newOrder);
    }
}
