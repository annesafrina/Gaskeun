package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;

import java.util.Date;

public interface OrderService {
    boolean validateOrder(Customer customer, Car car, Date startDate, Date endDate);
    Order createOrder(Customer customer, Car car, Date startDate,
                      Date endDate, String pickupLocation, String dropoffLocation);
    void cancelOrder(Customer customer, Order order);
    Order confirmOrRejectOrder(RentalProvider provider, Order order, OrderStatus status, String bookingMessage);
}
