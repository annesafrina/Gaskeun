package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.exception.IllegalUserAccessException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;

    private Order orderFromDto(OrderDto orderDto) throws ParseException {
        Order order = new Order();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        order.setPickupLocation(orderDto.getPickUpLocation());
        order.setDropoffLocation(orderDto.getDropOffLocation());
        order.setStartDate(formatter.parse(orderDto.getStartDate()));
        order.setEndDate(formatter.parse(orderDto.getEndDate()));

        return order;

    }

    @Override
    public Object[] validateOrder(Car car, Order order) {
        Object[] isValid = {true, null};

        if (!isValidByDate(order)) {
            isValid[0] = false;
            isValid[1] = "The end date precedes the starting date.";
            return isValid;
        }

        if (!isValidForCar(car, order)) {
            isValid[0] = false;
            isValid[1] = "The order is made for the dates where the car is unavailable.";
            return isValid;
        }

        if (!isValidDuringDate(car, order)) {
            isValid[0] = false;
            isValid[1] = "The car has already been booked for the selected date.";
        }

        return isValid;
    }

    private boolean isValidForCar(Car car, Order order) {

        Date carStartAvailable = car.getAvailableStartDate();
        Date carEndAvailable = car.getAvailableEndDate();

        return (carStartAvailable.compareTo(order.getStartDate()) <= 0 && carEndAvailable.compareTo(order.getEndDate()) >= 0);
    }

    private boolean isValidByDate(Order order) {
        Date startDate = order.getStartDate();
        Date endDate = order.getEndDate();

        return startDate.before(endDate);
    }

    private boolean isValidDuringDate(Car car, Order order) {
        List<Order> orderMadeUsingCar = orderRepository.findAllByCar(car);

        List<DateRange> unavailableDateRange = OrderUtils.findUnavailableDates(orderMadeUsingCar);
        for (DateRange dateRange: unavailableDateRange) {
            if (dateRange.dateInRange(order.getStartDate()) || dateRange.dateInRange(order.getEndDate())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Order createOrder(Customer customer, OrderDto orderDto) throws Exception{
        Car car;

        try {
            car = carRepository.findById(Long.parseLong(orderDto.getCarId())).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Car with id %s is not found", orderDto.getCarId()));
        }

        Order order = orderFromDto(orderDto);

        Object[] isValid = validateOrder(car, order);
        if (!(boolean) isValid[0]) {
            throw new IllegalStateException(String.valueOf(isValid[1]));
        }

        order.setCar(car);
        order.setVerified(false);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCustomer(customer);

        return orderRepository.save(order);
    }

    public Order getOrder(long id, UserDetails user) throws NoSuchElementException, IllegalStateException {
        Order order = orderRepository.getById(id);

        if (user instanceof Customer customer) {
            handleIllegalCustomer(order, customer);
        } else if (user instanceof RentalProvider provider) {
            handleIllegalProvider(order, provider);
        }

        return order;
    }

    private void handleIllegalCustomer(Order order, Customer customer) {
        if (order.getCustomer().getId() != customer.getId()) {
            throw new IllegalUserAccessException(order.getId(), customer.getEmail());
        }
    }

    private void handleIllegalProvider(Order order, RentalProvider provider) {
        if (!verifyOrderOwnership(provider, order)) {
            throw new IllegalUserAccessException(order.getId(), provider.getEmail());
        }

    }

    @Override
    public void cancelOrder(Customer customer, Order order) {

    }

    /**
     * Method to change the status and booking message of an order. This process is conducted by a rental provider.
     * Hence, before making any changes to the Order object, we first verify if the order indeed belongs to the
     * provider.
     *
     * If verified, the changes will be made and committed.
     *
     * @param provider=The provider object that receives the order from the customer
     * @param order=The order whose status and description is wanted to be changed
     * @param status=The status given by the rental provider to the order. When the customer chooses to confirm,
     *              the system must change the order status to WAITING_FOR_PAYMENT, else REJECTED.
     * @param bookingMessage=Description given by the rental provider regarding the booking/order
     *                      The field is nullable.
     * @return The edited order
     */
    @Override
    public Order confirmOrRejectOrder(RentalProvider provider, Order order, OrderStatus status, String bookingMessage) {
        return null;
    }


    /**
     * Verifies whether an order is destined to a rental provider
     * @param provider=The provider whose ownership over an order is to be verified
     * @param order=The order object to be tested
     * @return true if the car used in the order belongs to the provider, false if otherwise
     */
    public boolean verifyOrderOwnership(RentalProvider provider, Order order) {
        RentalProvider actualProvider = order.getCarProvider();
        return actualProvider.getId() == provider.getId();
    }
}
