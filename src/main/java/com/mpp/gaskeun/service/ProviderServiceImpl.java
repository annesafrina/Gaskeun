package com.mpp.gaskeun.service;

import com.mpp.gaskeun.ProviderRepository;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    private final ProviderRepository providerRepository;

    @Autowired
    private final BCryptPasswordEncoder encoder;

    @Override
    public Car addCar(RentalProvider provider, Car car) {
        return null;
    }

    @Override
    public Car deleteCar(RentalProvider provider, Car car) {
        return null;
    }

    @Override
    public Car getCarById(long id) {
        return null;
    }

    @Override
    public void addPolicy() {

    }

    @Override
    public RentalProvider register(RentalProvider provider) {
        log.info(String.format("Registering %s", provider.getUsername()));
        boolean exists = providerRepository.findByEmail(provider.getEmail()).isPresent();

        if(!exists) {
            String hashedPassword = encoder.encode(provider.getPassword());
            provider.setPassword(hashedPassword);
            return providerRepository.save(provider);
        } else {
            throw new IllegalStateException(String.format("Email %s already exists", provider.getEmail()));
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return providerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Not Found"
                ));
    }
}
