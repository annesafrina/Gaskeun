package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.CustomerReview;
import com.mpp.gaskeun.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Long> {
        Optional<CustomerReview> findByOrder(Order order);
        }

