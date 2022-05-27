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

    public static final String REDIRECT_URL = "redirect:/";
    public static final String ORDER_ATTRIB_NAME = "order";
    public static final String REVIEW_DTO_ATTRIB_NAME = "reviewDto";
    public static final String ERROR_ATTRIB_NAME = "error";
    public static final String REVIEW_TYPE_ATTRIB_NAME = "reviewType";
    public static final String REVIEW_PAGE_HTML = "review";
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/create/car/{orderId}")
    public String getCarReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof Customer)) {
            return REDIRECT_URL;
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CAR);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return REDIRECT_URL;
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute("orderStartDate", DateParser.parse(order.getStartDate()));
        model.addAttribute("orderEndDate", DateParser.parse(order.getEndDate()));
        model.addAttribute(ORDER_ATTRIB_NAME, order);
        model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
        return "car_review";
    }

    @PostMapping("/create/car")
    public String postCreateCarReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return REDIRECT_URL;
        }

        log.info("Creating Car Review");

        try {
            reviewDto.setReviewType(ReviewType.CAR);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CAR);
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
            model.addAttribute(ORDER_ATTRIB_NAME, order);
            model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
            return "car_review";
        }

        return REDIRECT_URL;
    }

    @GetMapping("/create/customer/{orderId}")
    public String getCustomerReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof RentalProvider)) {
            return REDIRECT_URL;
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CUSTOMER);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return REDIRECT_URL;
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute(REVIEW_TYPE_ATTRIB_NAME, "Customer");
        model.addAttribute(ORDER_ATTRIB_NAME, order);
        model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
        return REVIEW_PAGE_HTML;
    }

    @PostMapping("/create/customer")
    public String postCreateCustomerReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof RentalProvider)) {
            return REDIRECT_URL;
        }

        log.info("Creating Provider Review");

        try {
            reviewDto.setReviewType(ReviewType.CUSTOMER);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.CUSTOMER);
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
            model.addAttribute(REVIEW_TYPE_ATTRIB_NAME, "customer");
            model.addAttribute(ORDER_ATTRIB_NAME, order);
            model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
            return REVIEW_PAGE_HTML;
        }

        return REDIRECT_URL;
    }

    @GetMapping("/create/provider/{orderId}")
    public String getProviderReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user) {
        Order order;
        if(!(user instanceof Customer)) {
            return REDIRECT_URL;
        }

        try {
            Long parsedOrderId = Long.parseLong(orderId);
            order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.PROVIDER);

        } catch (NumberFormatException | OrderNotReviewableException e) {
            return REDIRECT_URL;
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);

        model.addAttribute(REVIEW_TYPE_ATTRIB_NAME, "Provider");
        model.addAttribute(ORDER_ATTRIB_NAME, order);
        model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
        return REVIEW_PAGE_HTML;
    }

    @PostMapping("/create/provider")
    public String postCreateProviderReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return REDIRECT_URL;
        }

        log.info("Creating Provider Review");

        try {
            reviewDto.setReviewType(ReviewType.PROVIDER);
            reviewService.submitReview(reviewDto);
        } catch (Exception e) {
            Long parsedOrderId = Long.parseLong(reviewDto.getOrderId());
            Order order = reviewService.validateOrderReviewable(parsedOrderId, user, ReviewType.PROVIDER);
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
            model.addAttribute(REVIEW_TYPE_ATTRIB_NAME, "Provider");
            model.addAttribute(ORDER_ATTRIB_NAME, order);
            model.addAttribute(REVIEW_DTO_ATTRIB_NAME, reviewDto);
            return REVIEW_PAGE_HTML;
        }

        return REDIRECT_URL;
    }


}

