package com.mhwang.sharding_implementation.controllers;


import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {

        customerService.createCustomer(customer);
        return ResponseEntity.status(201).body("User " + customer.getEmail() + " created successfully!");
    }

    @GetMapping("/find")
    public ResponseEntity<?> getCustomer(Long id) {

        Optional<Customer> customerOpt = customerService.findCustomer(id);

        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Customer not found");
        }

        return ResponseEntity.status(200).body(customerOpt.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {

        Customer updatedCustomer = customerService.updateCustomer(id, customer);

        if (updatedCustomer == null) {
            return ResponseEntity.status(400).body("unable to update customer");
        }

        return ResponseEntity.ok().body("Update successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.ok().body("");
    }
}
