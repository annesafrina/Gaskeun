package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Transmission;

import java.util.Date;
import java.util.List;

public interface SearchService {
    public List<Car> getCars();
    public List<Car> getCars(String cityName, Date startDate, Date endDate, int carCapacity, Transmission transmission,
                             long startPrice, long endPrice);
}
