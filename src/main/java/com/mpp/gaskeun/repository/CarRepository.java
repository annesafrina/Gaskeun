package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findCarByLicensePlate(String licensePlate);
}
