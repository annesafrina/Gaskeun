package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car addCar(RentalProvider provider, CarDto carDto) throws ParseException {
        /* Logic implementation before adding car to the repository */

        Car newCar = convertDtoToCar(carDto);
        Date startDate = newCar.getAvailableStartDate();
        Date endDate = newCar.getAvailableEndDate();

        /* Check if the date range is invalid */
        if(startDate.after(endDate)) {
            throw new IllegalStateException("Available start date should not be after available end date");
        }

        if(carRepository.findCarByLicensePlate(newCar.getLicensePlate()).isPresent()) {
            throw new IllegalStateException(
                    String.format("Car with license plate %s has been registered",
                            newCar.getLicensePlate()));
        }

        boolean todayIsInBetweenDateRange = new Date().after(startDate) && new Date().before(endDate);

        newCar.setAvailable(todayIsInBetweenDateRange);
        newCar.setRentalProvider(provider);

        Car saved = carRepository.save(newCar);

        log.info(saved.getLicensePlate());
        return newCar;
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
    public Location addLocation(Location location) {
        boolean exists = locationRepository.findByCityName(location.getCityName()).isPresent();

        if(location.getCityName().isBlank() || exists) {
            throw new IllegalStateException("Location has been registered or blank.");
        }

        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll(Sort.by(Sort.Direction.ASC, "cityName"));
    }

    /**
     * Popular Car entity with all attributes except
     * id, rating, isAvailable, and rentalProvider
     */
    private Car convertDtoToCar(CarDto carDto) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Location carLocation = locationRepository.findByCityName(carDto.getCityName())
                .orElse(null);

        Car car = new Car();
        car.setLicensePlate(carDto.getLicensePlate());
        car.setCapacity(carDto.getCapacity());
        car.setColor(Color.valueOf(carDto.getColor()));
        car.setTransmission(Transmission.valueOf(carDto.getTransmission()));
        car.setAvailableStartDate(format.parse(carDto.getAvailableStart()));
        car.setAvailableEndDate(format.parse(carDto.getAvailableEnd()));
        car.setPriceRate(carDto.getPriceRate());
        car.setModel(carDto.getModel());
        car.setLocation(carLocation);
        car.setPicture(carDto.getBase64image());
        car.setDescription(carDto.getDescription());

        return car;
    }


}
