package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.*;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.service.OrderService;
import com.mpp.gaskeun.service.ReviewService;
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
    public String getCarReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user,
                               @PathVariable("description") String description, @PathVariable("rating") double rating) {

        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);
        reviewDto.setDescription(description);
        reviewDto.setRating(rating);

        model.addAttribute("orderDto", reviewDto);
        return "car_review";
    }

    @PostMapping("/create/car")
    public String postCreateCarReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        log.info("Creating Car Review");

        CarReview carReview = new CarReview();
        carReview.setDescription(reviewDto.getDescription());
        carReview.setRating(reviewDto.getRating());
        carReview.setId(Long.parseLong(reviewDto.getOrderId()));
        try {
            reviewService.submitCarReviewAndRating((Customer) user, reviewDto, carReview);

        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviewDto", reviewDto);
            return "car_review";
        }


//        return "redirect:/order/display/" + review.getId();
        return  "redirect:/index/";
    }

    @GetMapping("/create/customer/{orderId}")
    public String getCustomerReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user,
                                    @PathVariable("description") String description, @PathVariable("rating") double rating) {
        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);
        reviewDto.setDescription(description);
        reviewDto.setRating(rating);


        model.addAttribute("orderDto", reviewDto);
        return "car_review";
    }

    @PostMapping("/create/customer")
    public String postCreateCustomerReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model
    ) {
        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        log.info("Creating Car Review");

        CustomerReview customerReview = new CustomerReview();
        customerReview.setDescription(reviewDto.getDescription());
        customerReview.setRating(reviewDto.getRating());
        customerReview.setId(Long.parseLong(reviewDto.getOrderId()));
        try {
            reviewService.submitCustomerReviewAndRating((RentalProvider) user, reviewDto, customerReview);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviewDto", reviewDto);
            return "car_review";
        }


//        return "redirect:/order/display/" + review.getId();
        return  "redirect:/index/";
    }

    @GetMapping("/create/customer/{orderId}")
    public String getProviderReview(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user,
                                    @PathVariable("description") String description, @PathVariable("rating") double rating) {
        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setOrderId(orderId);
        reviewDto.setDescription(description);
        reviewDto.setRating(rating);

        model.addAttribute("orderDto", reviewDto);
        return "car_review";
    }

    @PostMapping("/create/customer")
    public String postCreateProviderReview(ReviewDto reviewDto, @AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof Customer)) {
            return "redirect:/";
        }

        log.info("Creating Car Review");

        ProviderReview providerReview = new ProviderReview();
        providerReview.setDescription(reviewDto.getDescription());
        providerReview.setRating(reviewDto.getRating());
        providerReview.setId(Long.parseLong(reviewDto.getOrderId()));
        try {
            reviewService.submitProviderReviewAndRating((Customer) user, reviewDto, providerReview);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviewDto", reviewDto);
            return "car_review";
        }


//        return "redirect:/order/display/" + review.getId();
        return  "redirect:/index/";
    }

}

