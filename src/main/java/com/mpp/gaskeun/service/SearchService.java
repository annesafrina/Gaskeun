package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;

import java.text.ParseException;
import java.util.List;

public interface SearchService {
    List<Car> getCars();

    List<String> getCarNames();

    List<Car> getCars(String cityName, String startDate, String endDate, int carCapacity, String transmission, long minPrice, long maxPrice, String modelName) throws ParseException;
}
