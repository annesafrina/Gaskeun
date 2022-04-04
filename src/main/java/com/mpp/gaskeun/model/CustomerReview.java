package com.mpp.gaskeun.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "customer_review")
public class CustomerReview {

    //TODO: Create OneToOne Relationship with Order

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;
}
