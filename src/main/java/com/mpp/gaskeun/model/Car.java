package com.mpp.gaskeun.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "car")
@AllArgsConstructor
@NoArgsConstructor
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
    private double rating = 0;

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

    @Column(name = "picture", length = 65_535)
    private String picture;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private RentalProvider rentalProvider;

    @ManyToOne
    private Location location;

    @Column(name = "number_of_reviews")
    private int numberOfReviews = 0;

    public String getLocationName() {
        return location.getCityName();
    }

    public String getTransmissionName() {
        return transmission.toString();
    }

    public String getColorName() {
        return color.toString();
    }

    public boolean providerIsOwner(RentalProvider provider) {
        return this.rentalProvider.equals(provider);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && Objects.equals(licensePlate, car.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licensePlate);
    }
}
