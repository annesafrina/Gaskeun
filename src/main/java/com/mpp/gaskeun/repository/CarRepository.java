package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
