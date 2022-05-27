package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.Review;
import com.mpp.gaskeun.model.ReviewType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;

public interface ReviewService {
    Order validateOrderReviewable(Long id, UserDetails user, ReviewType reviewType);

    Review submitReview(ReviewDto reviewDto) throws NumberFormatException, NoSuchElementException;
}
