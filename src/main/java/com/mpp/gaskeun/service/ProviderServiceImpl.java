package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import com.mpp.gaskeun.utils.OrderUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor @Slf4j
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private final BCryptPasswordEncoder encoder;

    @Override
    public long getNumberOfCarRegistered(RentalProvider provider) {
        return carRepository.findAll().stream()
                .filter(car -> car.providerIsOwner(provider))
                .count();
    }

    @Override
    public List<Order> findAllOrders(RentalProvider provider) {
        return orderRepository.findAll().stream()
                .filter(order -> order.providerIsAssigned(provider))
                .toList();
    }

    @Override
    public List<OrderDisplayDto> findAllOrdersInDto(RentalProvider provider) {
        return findAllOrders(provider).stream()
                .map(OrderUtils::lightDisplayOrder)
                .toList();
    }

    @Override
    public RentalProvider update(RentalProvider provider, UserDto userDto) {
        if(!userDto.containNameAndPhoneNumber()) {
            throw new IncompleteFormException();
        }

        if(userDto.containsPassword()) {
            if(!encoder.matches(userDto.getOldPassword(), provider.getPassword())) {
                throw new IllegalArgumentException("Password does not match current password");
            }

            if(!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
                throw new IllegalArgumentException("New password does not match");
                
            } else {
                provider.setPassword(encoder.encode(userDto.getPassword()));
            }
        }

        provider.setName(userDto.getName());
        provider.setPhoneNumber(userDto.getPhoneNumber());
        return providerRepository.save(provider);
    }
}
