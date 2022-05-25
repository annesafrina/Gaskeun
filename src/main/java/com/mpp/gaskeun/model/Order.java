package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "created_date", nullable = false)
    private Date createdDate = new Date();

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "dropoff_location", nullable = false)
    private String dropoffLocation;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    public RentalProvider getCarProvider() {
        return this.car.getRentalProvider();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && isVerified == order.isVerified && Objects.equals(bookingMessage, order.bookingMessage) && Objects.equals(createdDate, order.createdDate) && Objects.equals(startDate, order.startDate) && Objects.equals(endDate, order.endDate) && Objects.equals(pickupLocation, order.pickupLocation) && Objects.equals(dropoffLocation, order.dropoffLocation) && Objects.equals(customer, order.customer) && Objects.equals(car, order.car) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingMessage, isVerified, createdDate, startDate, endDate, pickupLocation, dropoffLocation, customer, car, orderStatus);
    }
}
