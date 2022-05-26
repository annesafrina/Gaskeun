package com.mpp.gaskeun.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("customer")
@Table(name = "customer_review")
public class CustomerReview extends Review{
    @Override
    public void updateRevieweeRating() {
        Customer customer = this.order.getCustomer();
        double ratingSum = getRating() + customer.getRating();
        customer.setNumberOfReviews(customer.getNumberOfReviews() + 1);
        customer.setRating(ratingSum/customer.getNumberOfReviews());
    }

    @Override
    public void updateOrderReviewers() {
        this.order.setCustomerIsReviewed(true);
    }
}
