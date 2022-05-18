package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final BCryptPasswordEncoder encoder;

    @Override
    public List<Order> findAllOrders(Customer customer) {
        return null;
    }

    @Override
    public List<Order> findAllOnGoingOrders(Customer customer) {
        return null;
    }

}
