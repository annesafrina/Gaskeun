package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;

import java.security.Provider;

public interface ProviderService {

    Car addCar(Provider provider, Car car);
    Car deleteCar(Provider provider, Car car);
    Car getCarById(long id);
    void addPolicy();

}
