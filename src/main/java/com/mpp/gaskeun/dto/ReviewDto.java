package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ReviewDto {
    private String orderId;
    private String description;
    private double rating;
}
