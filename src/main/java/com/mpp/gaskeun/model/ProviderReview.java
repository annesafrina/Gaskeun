package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "provider_review")
@Setter @Getter
public class ProviderReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Order order;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;
}
