package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.OrderStatus;
import com.mpp.gaskeun.repository.CustomerRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.utils.OrderUtils;
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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findAllOrders(Customer customer) {
        //retrieve all order and convert to stream
        return orderRepository.findAll().stream().filter(order -> order.customerIsOwner(customer)).toList();
    }

    @Override
    public List<Order> findAllOnGoingOrders(Customer customer) {
        return orderRepository.findAll().stream()
                .filter(order -> order.customerIsOwner(customer))
                .filter(order -> !order.getOrderStatus().equals(OrderStatus.COMPLETED)).toList();

    }

    @Override
    public List<OrderDisplayDto> findAllOrdersInDto(Customer customer) {
        return findAllOrders(customer)
                .stream().map(OrderUtils::lightDisplayOrder).toList();
    }

    @Override
    public List<OrderDisplayDto> findAllOnGoingOrdersInDto(Customer customer) {
        return findAllOnGoingOrders(customer)
                .stream().map(OrderUtils::lightDisplayOrder).toList();
    }


    //Update
    @Override
    public Customer update(Customer customer, UserDto userDto) {
        if (!userDto.containNameAndPhoneNumber()) {
            throw new IncompleteFormException();
        }
        if (userDto.containsPassword()) {
            if (!encoder.matches(userDto.getOldPassword(), customer.getPassword())) {
                throw new IllegalArgumentException("Password does not match current password");
            }

            if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
                throw new IllegalArgumentException("New password does not match");

            } else {
                customer.setPassword(encoder.encode(userDto.getPassword()));
            }
        }

        customer.setName(userDto.getName());
        customer.setPhoneNumber(userDto.getPhoneNumber());
        return customerRepository.save(customer);
    }


}
