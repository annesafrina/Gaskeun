package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerService {
    public List<Order> findAllOrders(Customer customer);
    public List<Order> findAllOnGoingOrders(Customer customer);
    public Customer update(Customer customer, UserDto userDto);
}
