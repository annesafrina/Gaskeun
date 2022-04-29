package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.RentalProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ProviderService extends UserDetailsService {

    Car addCar(RentalProvider provider, Car car);
    Car deleteCar(RentalProvider provider, Car car);
    Car getCarById(long id);
    void addPolicy();
    RentalProvider register(RentalProvider provider);

}
