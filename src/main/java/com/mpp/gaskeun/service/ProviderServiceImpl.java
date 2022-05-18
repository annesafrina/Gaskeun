package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.RentalProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProviderServiceImpl implements ProviderService{

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
}
