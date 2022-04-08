package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.RentalProvider;

public interface ProviderService {

    Car addCar(RentalProvider provider, Car car);
    Car deleteCar(RentalProvider provider, Car car);
    Car getCarById(long id);
    void addPolicy();

}
