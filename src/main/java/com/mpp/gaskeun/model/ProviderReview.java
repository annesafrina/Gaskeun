package com.mpp.gaskeun.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("provider")
@Table(name = "provider_review")
public class ProviderReview extends Review {
    @Override
    public void updateRevieweeRating() {
        RentalProvider provider = this.order.getCarProvider();
        double ratingSum = getRating() + provider.getPerformanceRating();
        provider.setNumberOfReviews(provider.getNumberOfReviews() + 1);
        provider.setPerformanceRating(ratingSum / (provider.getNumberOfReviews() + 1));
    }

    @Override
    public void updateOrderReviewers() {
        this.order.setProviderIsReviewed(true);
    }
}
