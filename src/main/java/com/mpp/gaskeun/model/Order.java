package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "booking_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "booking_message")
    private String bookingMessage;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "dropoff_location", nullable = false)
    private String dropoffLocation;

    @ManyToOne
    private Car car;
}