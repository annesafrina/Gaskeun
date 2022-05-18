package com.mpp.gaskeun.service;

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

    @Override
    public UserDetails register(UserDetails user) throws IllegalStateException {
        log.info("Register {}", user.getUsername());
        boolean exists = getMatchingUserFromUsername(user.getUsername()) != null;

        if(!exists) {
            if(user instanceof Customer customer) {
                return registerCustomer(customer);

            } else {
                return registerProvider((RentalProvider) user);

            }
        } else {
            throw new IllegalStateException(String.format("Email %s already exists", user.getUsername()));
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
