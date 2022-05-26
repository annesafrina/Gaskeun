package com.mpp.gaskeun.model;

import javax.persistence.*;

@Entity
@DiscriminatorValue("car")
@Table(name = "car_review")
public class CarReview extends Review {
    @Override
    public void updateRevieweeRating() {
        Car car = this.order.getCar();
        double ratingSum = getRating() + car.getRating();
        car.setNumberOfReviews(car.getNumberOfReviews() + 1);
        car.setRating(ratingSum/car.getNumberOfReviews());
    }

    @Override
    public void updateOrderReviewers() {
        this.order.setCarIsReviewed(true);
    }
}
