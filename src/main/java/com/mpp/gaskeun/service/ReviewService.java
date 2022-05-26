package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.model.*;

import java.util.Date;

public interface ReviewService {
    boolean validateOrderIsCompleted(Order order);
    CarReview submitCarReviewAndRating(Customer customer, ReviewDto reviewDto, CarReview car_review) throws Exception;
    ProviderReview submitProviderReviewAndRating(Customer customer, ReviewDto reviewDto, ProviderReview provider_review) throws Exception ;
    CustomerReview submitCustomerReviewAndRating(RentalProvider provider, ReviewDto reviewDto, CustomerReview customer_review) throws Exception ;
}
