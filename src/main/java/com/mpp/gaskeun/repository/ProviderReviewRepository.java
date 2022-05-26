package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.ProviderReview;
import com.mpp.gaskeun.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderReviewRepository extends JpaRepository<ProviderReview, Long> {
    Optional<ProviderReview> findByOrder(Order order);
}

