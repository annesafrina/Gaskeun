package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.model.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface ReviewService {
    Order validateOrderReviewable(Long id, UserDetails user);
    Review submitReview(ReviewDto reviewDto) throws Exception;
}
