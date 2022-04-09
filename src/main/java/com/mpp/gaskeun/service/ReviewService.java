package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;

import java.util.Date;

public interface ReviewService {
    public boolean validateOrderIsCompleted(Customer customer, Order order);
    public CarReview submitCarReviewAndRating(Customer customer, Order order, CarReview car_review);
    public ProviderReview submitProviderReviewAndRating(Customer customer, Order order, ProviderReview provider_review);
    public CustomerReview submitCustomerReviewAndRating(RentalProvider provider, Order order, CustomerReview customer_review);
}
