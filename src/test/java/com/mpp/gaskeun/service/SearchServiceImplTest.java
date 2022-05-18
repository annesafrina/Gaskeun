package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private CarRepository carRepository;

    @Test
    void whenNoCarInRepository_getCarsShouldReturnEmptyList() {
        List<Car> expected = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(expected);
        List<Car> allCars = searchService.getCars();

        assertEquals(expected, allCars);
    }

    @Test
    void whenCarExistInRepository_getCarsShouldReturnAllCars() {
        Car car = new Car();
        List<Car> expected = List.of(car);
        when(carRepository.findAll()).thenReturn(expected);
        List<Car> allCars = searchService.getCars();

        assertEquals(expected, allCars);
    }
}