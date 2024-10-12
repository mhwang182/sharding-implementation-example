package com.mhwang.sharding_implementation.repository;

import com.mhwang.sharding_implementation.repository.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
