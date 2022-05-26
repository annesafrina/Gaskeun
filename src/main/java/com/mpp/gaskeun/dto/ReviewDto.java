package com.mpp.gaskeun.dto;

import com.mpp.gaskeun.model.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ReviewDto {
    private String orderId;
    private String description;
    private double rating;
    private ReviewType reviewType;

    public Review getReview() {
        if (reviewType == ReviewType.CAR) {
            return new CarReview();
        } else if (reviewType == ReviewType.CUSTOMER) {
            return new CustomerReview();
        } else if (reviewType == ReviewType.PROVIDER) {
            return new ProviderReview();
        }
        return null;
    }
}
