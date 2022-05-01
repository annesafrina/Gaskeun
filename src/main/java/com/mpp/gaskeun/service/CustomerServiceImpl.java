package com.mpp.gaskeun.service;

import com.mpp.gaskeun.repository.CustomerRepository;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Not Found"
                ));
    }

    @Override
    public Customer register(Customer newCustomer) throws IllegalStateException{
        log.info("Register");
        boolean exists = customerRepository.findByEmail(newCustomer.getEmail()).isPresent();

        if(!exists) {
            String hashedPassword = encoder.encode(newCustomer.getPassword());
            newCustomer.setPassword(hashedPassword);
            customerRepository.save(newCustomer);
            return newCustomer;

        } else {
            throw new IllegalStateException(String.format("Email %s already exists", newCustomer.getEmail()));
        }

    }
}
