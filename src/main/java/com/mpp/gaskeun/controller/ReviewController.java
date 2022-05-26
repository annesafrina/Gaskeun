package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.*;
import com.mpp.gaskeun.exception.OrderDoesNotExistException;
import com.mpp.gaskeun.exception.OrderNotReviewableException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.service.OrderService;
import com.mpp.gaskeun.service.ReviewService;
import com.mpp.gaskeun.utils.DateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/create/car/{orderId}")
    public String getCarReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CAR);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute("orderStartDate", DateParser.parse(order.getStartDate()));
        model.addAttribute("orderEndDate", DateParser.parse(order.getEndDate()));
        model.addAttribute("order", order);
        model.addAttribute("reviewDto", reviewDto);
        return "car_review";
    }

    @PostMapping("/create/car")
    public String postCreateCarReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        log.info("Creating Car Review");

        try {
            reviewDto.setReviewType(ReviewType.CAR);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CAR);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("order", order);
            model.addAttribute("reviewDto", reviewDto);
            return "car_review";
        }

        return  "redirect:/";
    }

    @GetMapping("/create/customer/{orderId}")
    public String getCustomerReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CUSTOMER);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute("reviewType", "Customer");
        model.addAttribute("order", order);
        model.addAttribute("reviewDto", reviewDto);
        return "review";
    }

    @PostMapping("/create/customer")
    public String postCreateCustomerReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        log.info("Creating Provider Review");

        try {
            reviewDto.setReviewType(ReviewType.CUSTOMER);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CUSTOMER);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviewType", "customer");
            model.addAttribute("order", order);
            model.addAttribute("reviewDto", reviewDto);
            return "review";
        }

        return  "redirect:/";
    }

    @GetMapping("/create/provider/{orderId}")
    public String getProviderReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.PROVIDER);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute("reviewType", "Provider");
        model.addAttribute("order", order);
        model.addAttribute("reviewDto", reviewDto);
        return "review";
    }

    @PostMapping("/create/provider")
    public String postCreateProviderReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        log.info("Creating Provider Review");

        try {
            reviewDto.setReviewType(ReviewType.PROVIDER);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.PROVIDER);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviewType", "Provider");
            model.addAttribute("order", order);
            model.addAttribute("reviewDto", reviewDto);
            return "review";
        }

        return  "redirect:/";
    }


}

