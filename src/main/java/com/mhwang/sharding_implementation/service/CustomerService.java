package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.ShardContext;
import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShardKeyGenerationService shardKeyGenerationService;

    public Optional<Customer> findCustomer(String id) {
        return customerRepository.findById(id);
    }

    public void createCustomer(Customer customer) {

        String id = shardKeyGenerationService.generateShardKey();
        customer.setId(id);

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(id));

        customerRepository.save(customer);
    }

    public Customer updateCustomer(String id, Customer customer) {
        return customerRepository
                .findById(id)
                .map(customerToUpdate -> {
                    customerToUpdate.setFirstname(customer.getFirstname());
                    customerToUpdate.setLastname(customer.getLastname());
                    customerToUpdate.setEmail(customer.getEmail());
                    return customerRepository.save(customerToUpdate);
                })
                .orElse(null);
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

}
