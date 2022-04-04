package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car_review")
@Getter @Setter
public class CarReview {
    @Id
    private long id;

    @OneToOne
    @MapsId
    private Order order;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;
}
