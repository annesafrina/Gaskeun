package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor @Slf4j
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private final BCryptPasswordEncoder encoder;

    @Override
    public long getNumberOfCarRegistered(RentalProvider provider) {
        return carRepository.findAll().stream()
                .filter(car -> car.providerIsOwner(provider))
                .count();
    }

    @Override
    public RentalProvider update(RentalProvider provider, UserDto userDto) {
        if(!userDto.isValid()) {
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
        provider.setPhoneNumber(userDto.getPhone_number());
        return providerRepository.save(provider);
    }
}
