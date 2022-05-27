package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;

import java.util.List;

public interface CustomerService {
    List<Order> findAllOrders(Customer customer);

    List<Order> findAllOnGoingOrders(Customer customer);

    Customer update(Customer customer, UserDto userDto);

    List<OrderDisplayDto> findAllOrdersInDto(Customer customer);

    List<OrderDisplayDto> findAllOnGoingOrdersInDto(Customer customer);
}
