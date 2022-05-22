package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.model.*;

import java.util.Date;

public interface OrderService {
    Object[] validateOrder(Car car, Order order);
    Order createOrder(Customer customer, OrderDto orderDto) throws Exception;
    void cancelOrder(Customer customer, Order order);
    Order confirmOrRejectOrder(RentalProvider provider, Order order, OrderStatus status, String bookingMessage);
}
