package com.mhwang.sharding_implementation.repository;

import com.mhwang.sharding_implementation.repository.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c JOIN FETCH c.orders orders WHERE c.id = :id")
    Customer fetchWithOrders(@Param("id") String id);
}
