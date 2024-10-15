package com.mhwang.sharding_implementation.repository;

import com.mhwang.sharding_implementation.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

}
