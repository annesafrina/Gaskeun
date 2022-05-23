package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.exception.CarDoesNotExistException;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.exception.NotCarOwnerException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CarRepository carRepository;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public boolean isCurrentlyAvailable(CarDto carDto) throws ParseException {
        Date today = new Date();
        Date startDate = dateFormatter.parse(carDto.getAvailableStart());
        Date endDate = dateFormatter.parse(carDto.getAvailableEnd());
        return today.after(startDate) && today.before(endDate);
    }

    @Override
    public Car addCar(RentalProvider provider, CarDto carDto) throws ParseException {
        if(isValidCarRegistration(carDto, true)) {
            Car car = new Car();
            initCarData(carDto, car);
            car.setRentalProvider(provider);

            log.info("Saved car {} in repository", car.getLicensePlate());
            return carRepository.save(car);
        }

        return null;
    }

    @Override
    public Car updateCar(RentalProvider provider, CarDto carDto) throws ParseException, CarDoesNotExistException {
        if(isValidCarRegistration(carDto, false)) {
            Car car = carRepository
                    .findCarByLicensePlate(carDto.getLicensePlate())
                    .orElseThrow(() -> new CarDoesNotExistException(carDto.getLicensePlate()));
            initCarData(carDto, car);

            if(providerIsNotCarOwner(provider, car))
                throw new NotCarOwnerException(provider.getEmail(), car.getLicensePlate());

            log.info("Updated car {} in repository", car.getLicensePlate());
            return carRepository.save(car);
        }

        return null;
    }

    @Override
    public Car deleteCar(RentalProvider provider, String licensePlate) {
        Car car = carRepository
                .findCarByLicensePlate(licensePlate)
                .orElseThrow(() -> new CarDoesNotExistException(licensePlate));

        if(providerIsNotCarOwner(provider, car))
            throw new NotCarOwnerException(provider.getEmail(), car.getLicensePlate());

        carRepository.delete(car);
        log.info("Car {} deleted from repository", car.getLicensePlate());
        return car;
    }

    @Override
    public Car getCarByLicensePlate(RentalProvider provider, String licensePlate) throws NotCarOwnerException {
        Car car = carRepository
                .findCarByLicensePlate(licensePlate)
                .orElseThrow(() -> new CarDoesNotExistException(licensePlate));

        if(providerIsNotCarOwner(provider, car)) {
           throw new NotCarOwnerException(provider.getEmail(), car.getLicensePlate());
        }

        return car;
    }

    @Override
    public Car getCarById(RentalProvider provider, long id) throws NotCarOwnerException, CarDoesNotExistException {
        Car car = carRepository
                .findById(id)
                .orElseThrow(() -> new CarDoesNotExistException(id));

        if(providerIsNotCarOwner(provider, car)) {
            throw new NotCarOwnerException(provider.getEmail(), car.getLicensePlate());
        }

        return car;
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

    private boolean isValidCarRegistration(CarDto carDto, boolean isNew) throws ParseException, IllegalStateException {
        Date startDate = dateFormatter.parse(carDto.getAvailableStart());
        Date endDate = dateFormatter.parse(carDto.getAvailableEnd());

        log.info("CarDto is valid? {}", carDto.isComplete());
        if(!carDto.isComplete()) {
            throw new IncompleteFormException();
        }

        if(startDate.after(endDate))
            throw new IllegalStateException("Available start date should not be after available end date");

        if(isNew && carRepository.findCarByLicensePlate(carDto.getLicensePlate()).isPresent())
            throw new IllegalStateException(
                    String.format("Car with license plate %s has been registered", carDto.getLicensePlate()));

        return true;
    }

    private boolean providerIsNotCarOwner(RentalProvider provider, Car car) {
        return !car.getRentalProvider().equals(provider);
    }

    /**
     * Popular Car entity with all attributes except
     * id, rating, isAvailable, and rentalProvider
     */
    private void initCarData(CarDto carDto, Car car) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Location carLocation = locationRepository.findByCityName(carDto.getCityName())
                .orElseThrow(() -> new IllegalStateException("Invalid City Name inserted"));

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
        car.setAvailable(isCurrentlyAvailable(carDto));
    }

}
