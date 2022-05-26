package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("provider")
@Table(name = "provider_review")
public class ProviderReview extends Review {
    @Override
    public void updateRevieweeRating() {
        RentalProvider provider = this.order.getCarProvider();
        double ratingSum = getRating() + provider.getPerformanceRating();
        provider.setNumberOfReviews(provider.getNumberOfReviews() + 1);
        provider.setPerformanceRating(ratingSum/provider.getNumberOfReviews());
    }
}
