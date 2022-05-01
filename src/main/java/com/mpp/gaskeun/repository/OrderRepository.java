package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
