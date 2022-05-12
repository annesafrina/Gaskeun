package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean validateOrder(Customer customer, Car car, Date startDate, Date endDate) {
        return false;
    }

    @Override
    public Order createOrder(Customer customer, Car car, Date startDate, Date endDate, String pickupLocation, String dropoffLocation) {
        return null;
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
        return false;
    }
}
