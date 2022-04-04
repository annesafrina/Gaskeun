package com.mpp.gaskeun.model;

import javax.persistence.*;

@Entity
@Table(name="Rental_Provider")
public class RentalProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="email", unique=true, nullable=false)
    private String email;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="performance_rating")
    private double performanceRatings;
}
