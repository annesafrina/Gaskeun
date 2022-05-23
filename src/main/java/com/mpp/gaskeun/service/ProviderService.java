package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.exception.CarDoesNotExistException;
import com.mpp.gaskeun.exception.NotCarOwnerException;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.model.RentalProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.ParseException;
import java.util.List;

public interface ProviderService {

    Car addCar(RentalProvider provider, CarDto carDto) throws ParseException;
    Car updateCar(RentalProvider provider, CarDto carDto) throws ParseException;
    Car deleteCar(RentalProvider provider, String licensePlate);
    Car getCarByLicensePlate(RentalProvider provider, String licensePlate) throws IllegalStateException;

    Car getCarById(RentalProvider provider, long id) throws NotCarOwnerException, CarDoesNotExistException;

    Location addLocation(Location location);
    List<Location> getAllLocations();

}
