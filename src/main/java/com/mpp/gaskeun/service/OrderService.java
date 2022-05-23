package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.model.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface OrderService {
    Object[] validateOrder(Car car, Order order);
    Order createOrder(Customer customer, OrderDto orderDto) throws Exception;
    Order getOrder(long id, UserDetails user);
    void cancelOrder(Customer customer, Order order);
    Order setOrderStatus(RentalProvider provider, Order order, OrderStatus status, String bookingMessage);
}
