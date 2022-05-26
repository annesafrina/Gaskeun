package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.exception.OrderDoesNotExistException;
import com.mpp.gaskeun.exception.OrderNotReviewableException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review submitReview(ReviewDto reviewDto) {
        Order order;
        Review review;

        try {
            order = orderRepository.findById(Long.parseLong(reviewDto.getOrderId())).get();
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Order with id %s is not found", reviewDto.getOrderId()));
        }

        review = reviewDto.getReview();
        review.setOrder(order);
        review.setRating(reviewDto.getRating());
        review.setDescription(review.getDescription());
        review.updateRevieweeRating();

        reviewRepository.save(review);
        return review;
    }

    @Override
    public Order validateOrderReviewable(Long id, UserDetails user) {
        try {
            Order order = validateOrderExists(id);
            if (!(validateOrderIsCompleted(order) && orderOwnedByUser(order, user))) {
                throw new OrderNotReviewableException(id);
            }
            return order;

        } catch (OrderDoesNotExistException e) {
            throw new OrderNotReviewableException(id);
        }
    }

    private boolean validateOrderIsCompleted(Order order) {
        OrderStatus status = order.getOrderStatus();
        return status.equals(OrderStatus.COMPLETED);
    }

    private boolean orderOwnedByUser(Order order,UserDetails user) {
        if (user instanceof Customer customer) {
            return order.getCustomer().getId() == customer.getId();
        } else if (user instanceof RentalProvider provider) {
            return order.getCarProvider().getId() == provider.getId();
        }
        return false;
    }

    private Order validateOrderExists(Long id) {
        Order order = orderRepository.findById(id).orElse(null) ;

        if (order != null) {
            return order;
        }

        throw new OrderDoesNotExistException(id);
    }
}
