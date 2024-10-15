package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.OrderRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.repository.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShardKeyGenerationService shardKeyGenerationService;

    @Transactional
    public Order createOrderAndAddToCustomer(String customerId, Long productSku) {

        Order newOrder = new Order();
        newOrder.setProductSku(productSku);

        customerRepository
                .findById(customerId)
                .map(customer -> {
                    newOrder.setCustomer(customer);
                    setOrderId(newOrder, customer);
                    return customer;
                });

        return orderRepository.save(newOrder);
    }

    public Optional<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(String orderId, Order order) {

        return orderRepository
                .findById(orderId)
                .map(orderToUpdate -> {
                    orderToUpdate.setProductSku(order.getProductSku());
                    orderRepository.save(orderToUpdate);
                    return orderToUpdate;
                })
                .orElse(null);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    private void setOrderId(Order order, Customer customer) {
        int shard = shardKeyGenerationService.getShardNumberFromKey(customer.getId());

        order.setId(shardKeyGenerationService.generateShardKey(shard));
    }
}
