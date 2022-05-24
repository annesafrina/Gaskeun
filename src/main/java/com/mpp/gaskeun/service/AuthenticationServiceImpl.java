package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CustomerDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CustomerRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final int DEFAULT_RATING = 5;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private final BCryptPasswordEncoder encoder;

    private void initDataCustomer(CustomerDto customerDto, Customer customer) {
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        customer.setDrivingLicenseNumber(customerDto.getDriving_license());
        customer.setIdCardNumber(customerDto.getId_card());
        customer.setName(customerDto.getName());
        customer.setPhoneNumber(customerDto.getPhone_number());
    }

    private void initDataProvider(UserDto userDto, RentalProvider provider) {
        provider.setEmail(userDto.getEmail());
        provider.setName(userDto.getName());
        provider.setPassword(userDto.getPassword());
    }

    @Override
    public UserDetails register(UserDto userDto) throws IllegalArgumentException {
        log.info("Register {}", userDto.getEmail());
        boolean exists = getMatchingUserFromUsername(userDto.getEmail()) != null;

        if(!exists) {
            if(userDto instanceof CustomerDto customerDto) {
                Customer customer = new Customer();
                initDataCustomer(customerDto, customer);
                return registerCustomer(customer);

            } else {
                RentalProvider provider = new RentalProvider();
                initDataProvider(userDto, provider);
                return registerProvider(provider);
            }

        } else {
            throw new IllegalArgumentException(String.format("Email %s already exists", userDto.getEmail()));
        }
    }

    private Customer registerCustomer(Customer customer) {
        String hashedPassword = encoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);
        customer.setRating(DEFAULT_RATING);

        customerRepository.save(customer);

        return customer;
    }

    private RentalProvider registerProvider(RentalProvider provider) {
        String hashedPassword = encoder.encode(provider.getPassword());
        provider.setPassword(hashedPassword);
        provider.setPerformanceRating(DEFAULT_RATING);

        providerRepository.save(provider);

        return provider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = getMatchingUserFromUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("Not Found");
        }

        return user;
    }

    private UserDetails getMatchingUserFromUsername(String username) {
        UserDetails user;

        user = customerRepository.findByEmail(username).orElse(null);

        if(user == null) {
            /* Search for provider with the username when customer does not exist */
            user = providerRepository.findByEmail(username).orElse(null);
        }

        return user;
    }
}
