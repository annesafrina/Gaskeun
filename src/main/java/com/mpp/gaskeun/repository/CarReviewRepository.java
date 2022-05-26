package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.CarReview;
import com.mpp.gaskeun.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarReviewRepository extends JpaRepository<CarReview, Long> {
    Optional<CarReview> findByOrder(Order order);
}
