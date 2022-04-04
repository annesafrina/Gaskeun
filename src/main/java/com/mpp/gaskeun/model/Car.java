package com.mpp.gaskeun.model;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "color")
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission")
    private Transmission transmission;

    @Column(name = "rating")
    private double rating;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "available_start_date")
    private Date availableStartDate;

    @Column(name = "available_end_date")
    private Date availableEndDate;

    @Column(name = "price_rate")
    private long priceRate;

    @Column(name = "model")
    private String model;
}
