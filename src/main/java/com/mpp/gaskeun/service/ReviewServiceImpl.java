package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private CarReviewRepository carReviewRepository;

    @Autowired
    private CustomerReviewRepository customerReviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ProviderReviewRepository providerReviewRepository;


    @Override
    public boolean validateOrderIsCompleted(Order order) {
        return isComplete(order);
    }

    @Override
    public CarReview submitCarReviewAndRating(Customer customer, ReviewDto reviewDto, CarReview car_review) throws Exception {
        Order order;
        Car car;

        try {
            order = orderRepository.findById(Long.parseLong(reviewDto.getOrderId())).get();
            car = order.getCar();

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Order with id %s is not found", reviewDto.getOrderId()));
        }

        if (validateOrderIsCompleted(order)){
            car.setNumberOfReviews(car.getNumberOfReviews() + 1);
            car.setRating(car.getRating() / car.getNumberOfReviews());
            return carReviewRepository.save(car_review);
        }
        return car_review;
    }

    @Override
    public ProviderReview submitProviderReviewAndRating(Customer customer, ReviewDto reviewDto, ProviderReview provider_review) throws Exception {
        Order order;
        RentalProvider provider;

        try {
            order = orderRepository.findById(Long.parseLong(reviewDto.getOrderId())).get();
            provider = order.getCarProvider();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Order with id %s is not found", reviewDto.getOrderId()));
        }

        if (validateOrderIsCompleted(order)){
            provider.setNumberOfReviews(provider.getNumberOfReviews() + 1);
            provider.setPerformanceRating(provider.getPerformanceRating() / provider.getNumberOfReviews());
            return providerReviewRepository.save(provider_review);
        }
        return provider_review;
    }

    @Override
    public CustomerReview submitCustomerReviewAndRating(RentalProvider provider, ReviewDto reviewDto, CustomerReview customer_review) throws Exception {
        Order order;
        Customer customer;

        try {
            order = orderRepository.findById(Long.parseLong(reviewDto.getOrderId())).get();
            customer = order.getCustomer();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Order with id %s is not found", reviewDto.getOrderId()));
        }

        if (validateOrderIsCompleted(order)){
            customer.setNumberOfReviews(provider.getNumberOfReviews() + 1);
            customer.setRating(provider.getPerformanceRating() / provider.getNumberOfReviews());
            return customerReviewRepository.save(customer_review);
        }
        return customer_review;
    }

    private boolean isComplete(Order order) {
        OrderStatus status = order.getOrderStatus();
        return status.equals(OrderStatus.COMPLETED);
    }
}
