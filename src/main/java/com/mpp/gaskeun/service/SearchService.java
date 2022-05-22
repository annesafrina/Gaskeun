package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;

import java.util.Date;
import java.util.List;

public interface SearchService {
    public List<Car> getCars();

    List<Car> getCars(String cityName, Date startDate, Date endDate, int carCapacity, Transmission transmission, long minPrice, long maxPrice, String modelName);
}
