package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private CarRepository carRepository;

    /**
     * Method to return all cars in the database provided by any car provider
     * @return List of all cars provided in the database
     * */
    @Override
    public List<Car> getCars() {
        return null;
    }

    /**
     * @param location=The required location (city) where the cars should be stationed
     * @param dateRange=The required date range where the car should be available: The car should not be part of any order
     *                 in this date range (orderStartDate <= endDate and orderEndDate >= startDate)
     * @param carCapacity=Required car capacity
     * @param transmission=Required car transmission
     * @param priceRange=The price of the car must be within this range (priceRange.startPrice <= price <= priceRange.endPrice)
     * @return A list of cars matching the required filter.
     *
     * Note: It is unnecessary for all field to be filled. If the value of a field is null (not provided by user), the field
     * will be neglected in the filtration process. That is, all cars that corresponds to any of the possible values of the
     * neglected field will be included as long as it complies to the remaining filters.
     */
    @Override
    public List<Car> getCars(Location location, DateRange dateRange, int carCapacity, Transmission transmission, PriceRange priceRange) {
        return null;
    }
}
