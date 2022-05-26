package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
}
