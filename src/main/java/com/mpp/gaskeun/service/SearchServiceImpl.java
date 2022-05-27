package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.Transmission;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.LocationRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Setter
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrderServiceImpl orderService;

    /**
     * Method to return all cars in the database provided by any car provider
     *
     * @return List of all cars provided in the database
     */
    @Override
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    @Override
    public List<String> getCarNames() {
        return carRepository.findAll().stream().map(Car::getModel).toList();
    }

    /**
     * @param cityName=The          required location (city) where the cars should be stationed
     * @param startDate=The         starting date where the car should be available: The car should not be part of any order
     *                              in this date range (orderStartDate <= endDate and orderEndDate >= startDate)
     * @param endDate=The           end date where the car should be available: The car should not be part of any order
     *                              in this date range (orderStartDate <= endDate and orderEndDate >= startDate)
     * @param carCapacity=Required  car capacity
     * @param transmission=Required car transmission
     * @param maxPrice=The          price of the car must be within this range (minPrice <= price <= maxPrice)
     * @param minPrice=The          price of the car must be within this range (minPrice <= price <= maxPrice)
     * @param modelName=The         model name of the car
     * @return A list of cars matching the required filter.
     * <p>
     * Note: It is unnecessary for all field to be filled. If the value of a field is null (not provided by user), the field
     * will be neglected in the filtration process. That is, all cars that corresponds to any of the possible values of the
     * neglected field will be included as long as it complies to the remaining filters.
     */
    @Override
    public List<Car> getCars(String cityName, String startDate, String endDate, String transmission, long minPrice, long maxPrice, String modelName) throws ParseException {
        Order dummyOrder = createDummyOrder(startDate, endDate);
        Location location = locationRepository.findByCityName(cityName).orElse(null);
        return carRepository.findAll()
                .stream()
                .filter(location != null ? car -> car.getLocation().equals(location) : car -> true)
                .filter(dummyOrder == null ? car -> true : car -> orderService.isValidDuringDate(car, dummyOrder) && orderService.isValidForCar(car, dummyOrder))
                .filter(transmission.length() == 0 ? car -> true : car -> car.getTransmission() == Transmission.valueOf(transmission.toUpperCase()))
                .filter(car -> car.getPriceRate() >= minPrice && (car.getPriceRate() <= maxPrice || maxPrice == 0))
                .filter(modelName.length() == 0 ? car -> true : car -> car.getModel().equalsIgnoreCase(modelName))
                .toList();
    }


    private Order createDummyOrder(String startDate, String endDate) throws ParseException {
        Date parsedStartDate;
        Date parsedEndDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (startDate.length() == 0 && endDate.length() == 0) {
            return null;
        } else if (startDate.length() == 0) {
            parsedEndDate = format.parse(endDate);
            cal.setTime(parsedEndDate);
            cal.add(Calendar.DATE, -1);
            parsedStartDate = cal.getTime();
        } else if (endDate.length() == 0) {
            parsedStartDate = format.parse(startDate);
            cal.setTime(parsedStartDate);
            cal.add(Calendar.DATE, 1);
            parsedEndDate = cal.getTime();
        } else {
            parsedStartDate = format.parse(startDate);
            parsedEndDate = format.parse(endDate);
        }

        Order dummyOrder = new Order();
        dummyOrder.setStartDate(parsedStartDate);
        dummyOrder.setEndDate(parsedEndDate);
        log.info("{} {}", parsedStartDate, parsedEndDate);

        return dummyOrder;
    }
}
