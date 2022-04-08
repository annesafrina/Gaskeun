package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;

import java.util.List;

public interface CustomerService {
    public List<Order> findAllOrders(Customer customer);
    public List<Order> findAllOnGoingOrders(Customer customer);
}
