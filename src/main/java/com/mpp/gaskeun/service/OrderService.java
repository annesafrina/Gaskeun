package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.model.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.util.NoSuchElementException;

public interface OrderService {
    Object[] validateOrder(Car car, Order order);
    Order createOrder(Customer customer, OrderDto orderDto) throws NoSuchElementException, ParseException;
    Order getOrder(long id, UserDetails user);
    void cancelOrder(Customer customer, Order order);
    Order setOrderStatus(Order order, OrderStatus status, String bookingMessage);
}
