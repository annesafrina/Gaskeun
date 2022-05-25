package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;

import java.util.Date;

public interface ReviewService {
    boolean validateOrderIsCompleted(Customer customer, Order order);
    CarReview submitCarReviewAndRating(Customer customer, Order order, CarReview carReview);
    ProviderReview submitProviderReviewAndRating(Customer customer, Order order, ProviderReview providerReview);
    CustomerReview submitCustomerReviewAndRating(RentalProvider provider, Order order, CustomerReview customerReview);
}
