package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.exception.CarDoesNotExistException;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.exception.NotCarOwnerException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    public static final String LICENSE_PLATE = "VALID LICENSE PLATE";
    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CarRepository carRepository;

    private CarDto validCarDto;

    private CarDto invalidDateCarDto;

    private RentalProvider firstProvider;

    private RentalProvider secondProvider;

    private Location location;

    @BeforeEach
    void setupValidCarDto() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -5);
        Date startDate = calendar.getTime();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -3);
        Date endDate = calendar.getTime();

        validCarDto = new CarDto();
        validCarDto.setAvailableStart(dateFormatter.format(startDate));
        validCarDto.setAvailableEnd(dateFormatter.format(endDate));
        validCarDto.setLicensePlate(LICENSE_PLATE);
        validCarDto.setCapacity(4);
        validCarDto.setTransmission(Transmission.AUTOMATIC.name());
        validCarDto.setPriceRate(120_000);
        validCarDto.setColor(Color.WHITE.name());
        validCarDto.setModel("VALID MODEL");
        validCarDto.setBase64image("dummy base 64 image");
        validCarDto.setDescription("DUMMY DESCRIPTION");
        validCarDto.setCityName("City Name");
    }

    @BeforeEach
    void setupInvalidDateCarDto() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 3);
        Date startDate = calendar.getTime();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -3);
        Date endDate = calendar.getTime();

        String valid_license_plate = "VALID LICENSE PLATE";

        invalidDateCarDto = new CarDto();
        invalidDateCarDto.setAvailableStart(dateFormatter.format(startDate));
        invalidDateCarDto.setAvailableEnd(dateFormatter.format(endDate));
        invalidDateCarDto.setLicensePlate(valid_license_plate);
        invalidDateCarDto.setCapacity(4);
        invalidDateCarDto.setTransmission(Transmission.AUTOMATIC.name());
        invalidDateCarDto.setPriceRate(120_000);
        invalidDateCarDto.setColor(Color.WHITE.name());
        invalidDateCarDto.setModel("VALID MODEL");
        invalidDateCarDto.setBase64image("dummy base 64 image");
        invalidDateCarDto.setDescription("DUMMY DESCRIPTION");
        invalidDateCarDto.setCityName("City Name");
    }

    @BeforeEach
    void setupFirstProvider() {
        firstProvider = new RentalProvider();
        firstProvider.setId(1);
        firstProvider.setEmail("email@email.com");
        firstProvider.setPassword("encryptedPassword");
        firstProvider.setPhoneNumber("0122883");
        firstProvider.setPerformanceRating(5);
    }

    @BeforeEach
    void setupSecondProvider() {
        secondProvider = new RentalProvider();
        secondProvider.setId(2);
        secondProvider.setEmail("pesan@email.com");
    }

    @BeforeEach
    void setupLocation() {
        location = new Location();
        String cityName = "City Name";
        location.setId(1);
        location.setCityName(cityName);
    }

    @Test
    void whenTodayWithinStartDateAndEndDate_mustReturnTrue() throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();

        CarDto withinTodayDto = new CarDto();
        withinTodayDto.setAvailableStart(dateFormatter.format(yesterday));
        withinTodayDto.setAvailableEnd(dateFormatter.format(tomorrow));

        assertTrue(carService.isCurrentlyAvailable(withinTodayDto));
    }

    @Test
    void whenTodayNotWithinStartDateAndEndDate_mustReturnFalse() throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, -5);
        Date startDate = calendar.getTime();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, -3);
        Date endDate = calendar.getTime();

        CarDto notWithinTodayDto = new CarDto();
        notWithinTodayDto.setAvailableStart(dateFormatter.format(startDate));
        notWithinTodayDto.setAvailableEnd(dateFormatter.format(endDate));

        assertFalse(carService.isCurrentlyAvailable(notWithinTodayDto));
    }

    @Test
    void whenRegistrationAndValid_mustReturnTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isValidCarRegistration = carService.getClass()
                .getDeclaredMethod("isValidCarRegistration", CarDto.class, boolean.class);
        isValidCarRegistration.setAccessible(true);

        when(carRepository.findCarByLicensePlate(validCarDto.getLicensePlate())).thenReturn(Optional.empty());

        boolean isValid = (boolean) isValidCarRegistration.invoke(carService, validCarDto, true);
        assertTrue(isValid);
    }

    @Test
    void whenRegistrationIsNotComplete_mustThrowIncompleteFormException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CarDto notCompleteDto = new CarDto();
        Method isValidCarRegistration = carService.getClass()
                .getDeclaredMethod("isValidCarRegistration", CarDto.class, boolean.class);
        isValidCarRegistration.setAccessible(true);

        assertThrows(IncompleteFormException.class, () -> {
            try {
                isValidCarRegistration.invoke(carService, notCompleteDto, true);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void whenRegistrationStartDateEarlierThanEndDate_mustThrowIllegalArgumentException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isValidCarRegistration = carService.getClass()
                .getDeclaredMethod("isValidCarRegistration", CarDto.class, boolean.class);

        isValidCarRegistration.setAccessible(true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                isValidCarRegistration.invoke(carService, invalidDateCarDto, false);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void whenRegistrationValidButCarAlreadyRegistered_mustReturnIllegalArgumentException() throws NoSuchMethodException {
        Method isValidCarRegistration = carService.getClass()
                .getDeclaredMethod("isValidCarRegistration", CarDto.class, boolean.class);

        isValidCarRegistration.setAccessible(true);
        when(carRepository.findCarByLicensePlate(validCarDto.getLicensePlate())).thenReturn(Optional.of(new Car()));

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                isValidCarRegistration.invoke(carService, validCarDto, true);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void whenRepositoryIsEmpty_mustReturnEmptyList() {
        when(carRepository.findAll()).thenReturn(List.of());
        List<Car> ownedCars = carService.getAllCar(firstProvider);

        assertEquals(List.of(), ownedCars);
    }

    @Test
    void whenRepositoryIsNotEmpty_onlyReturnMatchingCars() {
        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();
        car1.setRentalProvider(firstProvider);
        car2.setRentalProvider(firstProvider);
        car3.setRentalProvider(secondProvider);

        List<Car> firstProviderCars = List.of(car1, car2);
        List<Car> repositoryCars = List.of(car1, car2, car3);

        when(carRepository.findAll()).thenReturn(repositoryCars);

        List<Car> matchingCars = carService.getAllCar(firstProvider);
        assertEquals(firstProviderCars, matchingCars);
    }

    @Test
    void whenLocationDoesNotExist_mustSaveAndReturnLocation() {
        when(locationRepository.findByCityName(location.getCityName())).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);
        Location returnedLocation = carService.addLocation(location);
        verify(locationRepository, times(1)).save(location);

        assertEquals(location, returnedLocation);
    }

    @Test
    void whenLocationExists_mustNotSaveAndThrowIllegalArgumentException() {
        when(locationRepository.findByCityName(location.getCityName())).thenReturn(Optional.of(location));

        assertThrows(IllegalArgumentException.class, () -> {
            carService.addLocation(location);
        });
    }

    @Test
    void whenCarWithIdDoesNotExist_mustThrowCarDoesNotExistException() {
        long carId = 0;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(CarDoesNotExistException.class, () -> {
            carService.getCarById(firstProvider, carId);
        });
    }

    @Test
    void whenCarWithIdExistsButDoesNotBelongToProvider_mustThrowNotCarOwnerException() {
        Car car = new Car();
        long id = 1;
        car.setId(id);
        car.setRentalProvider(firstProvider);

        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        assertThrows(NotCarOwnerException.class, () -> {
            carService.getCarById(secondProvider, id);
        });
    }

    @Test
    void whenCarWithIdExistsAndProviderIsOwner_mustReturnCar() {
        Car car = new Car();
        long id = 1;
        car.setId(id);
        car.setRentalProvider(firstProvider);

        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        Car returnedCar = carService.getCarById(firstProvider, id);

        assertEquals(car, returnedCar);
    }

    @Test
    void whenCarWithLicenseNotExists_mustThrowCarDoesNotExistException() {
        when(carRepository.findCarByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.empty());

        assertThrows(CarDoesNotExistException.class, () -> {
           carService.getCarByLicensePlate(firstProvider, LICENSE_PLATE);
        });
    }

    @Test
    void whenCarWithLicenseExistsButProviderNotOwner_mustThrowNotCarOwnerException() {
        Car car = new Car();
        car.setLicensePlate(LICENSE_PLATE);
        car.setRentalProvider(firstProvider);

        when(carRepository.findCarByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.of(car));

        assertThrows(NotCarOwnerException.class, () -> {
            carService.getCarByLicensePlate(secondProvider, LICENSE_PLATE);
        });
    }

    @Test
    void whenCarWithLicenseExistAndProviderIsOwner_mustReturnCar() {
        Car car = new Car();
        long id = 1;
        car.setId(id);
        car.setLicensePlate(LICENSE_PLATE);
        car.setRentalProvider(firstProvider);

        when(carRepository.findCarByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.of(car));
        Car returnedCar = carService.getCarByLicensePlate(firstProvider, LICENSE_PLATE);

        assertEquals(car, returnedCar);
    }

    @Test
    void whenAddCarRegistrationIsNotValid_mustThrowIllegalArgumentException() throws NoSuchMethodException {
        CarDto carDto = new CarDto();
        Method isValidCarRegistration = carService.getClass()
                .getDeclaredMethod("isValidCarRegistration", CarDto.class, boolean.class);

        isValidCarRegistration.setAccessible(true);

        assertThrows(IllegalArgumentException.class, () -> {
           carService.addCar(firstProvider, carDto);
        });
    }

    @Test
    void whenAddCarRegistrationIsValid_mustSave() throws ParseException {
        when(locationRepository.findByCityName(validCarDto.getCityName())).thenReturn(Optional.of(new Location()));
        carService.addCar(firstProvider, validCarDto);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void whenCarRegistrationIsInvalid_mustThrowIllegalArgumentException() {
        CarDto carDto = new CarDto();
        assertThrows(IllegalArgumentException.class, () -> {
           carService.updateCar(firstProvider, carDto);
        });
    }

    @Test
    void whenCarRegistrationIsValidButCarDoesNotBelongToProvider_mustThrowNotCarOwnerException() {
        Car carNotOwned = new Car();
        carNotOwned.setRentalProvider(secondProvider);

        when(locationRepository.findByCityName(validCarDto.getCityName())).thenReturn(Optional.of(new Location()));
        when(carRepository.findCarByLicensePlate(validCarDto.getLicensePlate())).thenReturn(Optional.of(carNotOwned));
        assertThrows(NotCarOwnerException.class, () -> {
            carService.updateCar(firstProvider, validCarDto);
        });
    }

    @Test
    void whenCarRegistrationIsValidAndProviderIsOwner_mustCallSave() throws ParseException {
        Car carNotOwned = new Car();
        carNotOwned.setRentalProvider(firstProvider);
        when(carRepository.findCarByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.of(carNotOwned));
        when(locationRepository.findByCityName(validCarDto.getCityName())).thenReturn(Optional.of(new Location()));
        carService.updateCar(firstProvider, validCarDto);
        verify(carRepository, times(1)).save(any(Car.class));
    }



}