package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCar(Car car);

}
