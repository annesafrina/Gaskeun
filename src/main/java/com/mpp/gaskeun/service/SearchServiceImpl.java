package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrderServiceImpl orderService;
    /**
     * Method to return all cars in the database provided by any car provider
     * @return List of all cars provided in the database
     * */
    @Override
    public List<Car> getCars() {
        return null;
    }

    /**
     * @param cityName=The required location (city) where the cars should be stationed
     * @param startDate=The starting date where the car should be available: The car should not be part of any order
     *                 in this date range (orderStartDate <= endDate and orderEndDate >= startDate)
     * @param endDate=The end date where the car should be available: The car should not be part of any order
     *                in this date range (orderStartDate <= endDate and orderEndDate >= startDate)
     * @param carCapacity=Required car capacity
     * @param transmission=Required car transmission
     * @param maxPrice=The price of the car must be within this range (minPrice <= price <= maxPrice)
     * @param minPrice=The price of the car must be within this range (minPrice <= price <= maxPrice)
     * @param modelName=The model name of the car
     * @return A list of cars matching the required filter.
     *
     * Note: It is unnecessary for all field to be filled. If the value of a field is null (not provided by user), the field
     * will be neglected in the filtration process. That is, all cars that corresponds to any of the possible values of the
     * neglected field will be included as long as it complies to the remaining filters.
     */
    @Override
    public List<Car> getCars(String cityName, Date startDate, Date endDate, int carCapacity, Transmission transmission, long minPrice, long maxPrice, String modelName) {
        Order dummyOrder = new Order();
        dummyOrder.setStartDate(startDate);
        dummyOrder.setEndDate(endDate);
        Location location = locationRepository.findByCityName(cityName).orElse(null);
        List<Car> allCars = carRepository.findAll()
                .stream()
                .filter(location != null ? car -> car.getLocation().equals(location) : car -> true)
                .filter(startDate != null ? car -> orderService.isValidDuringDate(car, dummyOrder) : car -> true)

                .toList();

        return allCars;
    }
}
