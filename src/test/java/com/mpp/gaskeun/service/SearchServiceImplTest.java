package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.Transmission;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Spy
    OrderServiceImpl orderService = new OrderServiceImpl();

    @Mock
    private CarRepository carRepository;

    @Mock
    private LocationRepository locationRepository;

    @Spy
    private OrderRepository orderRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    void setupCarInRepository() throws ParseException {
        orderService.setOrderRepository(orderRepository);
    }

    @Test
    void whenAllFilterIsApplied_carShouldExist() throws ParseException {
        Location location1 = new Location();
        location1.setCityName("Jakarta");

        Car car1 = new Car();
        car1.setLocation(location1);
        car1.setCapacity(5);
        car1.setTransmission(Transmission.AUTOMATIC);
        car1.setModel("Honda CRV");

        doReturn(List.of(car1)).when(carRepository).findAll();
        doReturn(Optional.of(location1)).when(locationRepository).findByCityName("Jakarta");
        doReturn(List.of()).when(orderRepository).findAllByCar(car1);
        doReturn(true).when(orderService).isValidForCar(any(Car.class), any(Order.class));

        List<Car> cars = searchService.getCars(
                "Jakarta",
                "2022-04-15",
                "2022-04-16",
                "automatic",
                0,
                1001,
                "Honda CRV"

        );
        assertNotEquals(0, cars.size());
    }

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