package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;

import java.util.Date;
import java.util.List;

public interface SearchService {
    public List<Car> getCars();

    List<Car> getCars(Location location, DateRange dateRange, int carCapacity, Transmission transmission, PriceRange priceRange);
}
