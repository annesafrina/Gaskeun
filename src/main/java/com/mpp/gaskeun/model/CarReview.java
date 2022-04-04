package com.mpp.gaskeun.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "car_review")
public class CarReview {

    //TODO: Create OneToOne Relationship with Order

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;
}
