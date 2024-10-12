package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.OrderRepository;
import com.mhwang.sharding_implementation.repository.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Order createOrderAndAddToCustomer(Long customerId, Long productSku) {

        Order newOrder = new Order();
        newOrder.setProductSku(productSku);

        customerRepository
                .findById(customerId)
                .map(customer -> {
                    newOrder.setCustomer(customer);
                    return customer;
                });

        return orderRepository.save(newOrder);
    }
}
