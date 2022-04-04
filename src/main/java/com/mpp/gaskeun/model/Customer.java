package com.mpp.gaskeun.model;

import javax.persistence.*;


@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="id_card_number", nullable = false)
    private String idCardNumber;

    @Column(name="driving_license_number", nullable = false)
    private String drivingLicenseNumber;

    @Column(name="rating")
    private double rating;
}
