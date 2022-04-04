package com.mpp.gaskeun.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "provider_review")
public class ProviderReview {

    //TODO: Create OneToOne Relationship with Order

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;
}
