package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", nullable = false)
    private Transmission transmission;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Column(name = "available_start_date")
    private Date availableStartDate;

    @Column(name = "available_end_date")
    private Date availableEndDate;

    @Column(name = "price_rate", nullable = false)
    private long priceRate;

    @Column(name = "model", nullable = false)
    private String model;

    @ManyToMany
    @JoinTable(
            name = "car_facilities",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private Set<Facility> availableFacilities;
}
