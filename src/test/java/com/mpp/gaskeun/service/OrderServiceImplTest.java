package com.mpp.gaskeun.service;

import com.mpp.gaskeun.exception.IllegalUserAccessException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository repository;

    @Test
    void whenOrderIsNotNullAndAssignedToProvider_mustReturnTrue() {
        var rentalProvider = new RentalProvider();
        var car = new Car();
        var order = new Order();

        car.setRentalProvider(rentalProvider);
        order.setCar(car);


        boolean isOwner = orderService.verifyOrderOwnership(rentalProvider, order);

        assertTrue(isOwner);
    }

    @Test
    void whenOrderNullAndNotOwnedByProvider_mustReturnFalse() {
        var rentalProvider = new RentalProvider();
        boolean isOwner = orderService.verifyOrderOwnership(rentalProvider, null);
        assertFalse(isOwner);
    }

    @Test
    void whenOrderNotNullAndNotOwnedByProvider_mustReturnFalse() {
        var rentalProviderA = new RentalProvider();
        rentalProviderA.setId(1);

        var rentalProviderB = new RentalProvider();
        rentalProviderB.setId(2);

        var car = new Car();
        var order = new Order();

        car.setRentalProvider(rentalProviderA);
        order.setCar(car);

        boolean isOwner = orderService.verifyOrderOwnership(rentalProviderB, order);
        assertFalse(isOwner);
    }

    @Test
    void whenBookingMessageIsNullAndStatusRejected_throwsError() {
        String bookingMessage = null;
        var status = OrderStatus.REJECTED;
        var order = new Order();

        assertThrows(IllegalStateException.class, () -> {
            orderService.setOrderStatus(order, status, bookingMessage);
        });
    }

    @Test
    void whenBookingMessageIsEmptyAndStatusRejected_throwsError() {
        String bookingMessage = "";
        var status = OrderStatus.REJECTED;
        var order = new Order();

        assertThrows(IllegalStateException.class, () -> {
            orderService.setOrderStatus(order, status, bookingMessage);
        });
    }

    @Test
    void whenBookingMessageNotEmptyAndStatusRejected_savesNewStatusToRepository() {
        String bookingMessage = "Weeeee";
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.setOrderStatus(order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageNullAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = null;
        var status = OrderStatus.WAITING_FOR_PAYMENT;
        var order = new Order();

        var result = orderService.setOrderStatus(order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageEmptyAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = "";
        var status = OrderStatus.WAITING_FOR_PAYMENT;
        var order = new Order();

        var result = orderService.setOrderStatus(order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertNull(result.getBookingMessage());
    }

    @Test
    void whenBookingMessageNotEmptyAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = "Bwrelebek";
        var status = OrderStatus.REJECTED;
        var order = new Order();

        var result = orderService.setOrderStatus(order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenEndDatePreceedesStartDate_isInvalid() {
        Order order = new Order();
        Car car = new Car();

        order.setStartDate(new Date(2022, 10, 5));
        order.setEndDate(new Date(2022, 9, 5));

        Object[] isValid = orderService.validateOrder(car, order);

        assertEquals(false, isValid[0]);
        assertEquals("The end date precedes the starting date.", isValid[1]);

    }

    @Test
    void whenCarIsNotAvailable_isInvalid() {
        Order order = new Order();
        Car car = new Car();

        car.setAvailableStartDate(new Date(2022, 5, 1));
        car.setAvailableEndDate(new Date(2022, 5, 6));

        order.setStartDate(new Date(2023, 3, 2));
        order.setEndDate(new Date(2023, 3, 4));

        Object[] isValid = orderService.validateOrder(car, order);
        assertEquals(false, isValid[0]);
        assertEquals("The order is made for the dates where the car is unavailable.", isValid[1]);
    }

    @Test
    void whenCarIsTakenDuringDate_isInvalid() {
        Order initialOrder = new Order();
        Car car = new Car();

        car.setAvailableStartDate(new Date(2022, 1, 1));
        car.setAvailableEndDate(new Date(2024, 10, 1));

        initialOrder.setStartDate(new Date(2022, 5, 1));
        initialOrder.setEndDate(new Date(2022, 5, 10));


        List<Order> allOrdersByCar = List.of(initialOrder);

        when(repository.findAllByCar(car)).thenReturn(allOrdersByCar);

        Order currentOrder = new Order();
        currentOrder.setStartDate(new Date(2022, 5, 2));
        currentOrder.setEndDate(new Date(2022, 5, 5));

        Object[] isValid = orderService.validateOrder(car, currentOrder);

        assertEquals(false, isValid[0]);
        assertEquals("The car has already been booked for the selected date.", isValid[1]);

    }

    @Test
    void whenOrderStarts60DaysFromNow_isInvalid() {
        Car car = new Car();
        Order order = new Order();

        Calendar calendar = Calendar.getInstance();

        car.setAvailableStartDate(calendar.getTime());

        calendar.add(Calendar.DATE, 60);
        Date start = calendar.getTime();

        calendar.add(Calendar.DATE, 10);
        Date end = calendar.getTime();

        order.setStartDate(start);
        order.setEndDate(end);

        calendar.add(Calendar.DATE, 5);
        car.setAvailableEndDate(calendar.getTime());


        Object[] isValid = orderService.validateOrder(car, order);
        assertEquals(false, isValid[0]);
        assertEquals("The car should only be book at most 30 days from now", isValid[1]);
    }

    @Test
    void whenOrderLengthIs20Days_isInvalid() {
        Car car = new Car();
        Order order = new Order();

        Calendar calendar = Calendar.getInstance();

        car.setAvailableStartDate(calendar.getTime());

        calendar.add(Calendar.DATE, 5);
        Date start = calendar.getTime();

        calendar.add(Calendar.DATE, 45);
        Date end = calendar.getTime();

        order.setStartDate(start);
        order.setEndDate(end);

        calendar.add(Calendar.DATE, 5);
        car.setAvailableEndDate(calendar.getTime());


        Object[] isValid = orderService.validateOrder(car, order);
        assertEquals(false, isValid[0]);
        assertEquals("Orders length is at most 30 days", isValid[1]);
    }

    @Test
    void whenEverythingIsGood_isValid() {
        Car car = new Car();
        Order order = new Order();

        Calendar calendar = Calendar.getInstance();

        car.setAvailableStartDate(calendar.getTime());

        calendar.add(Calendar.DATE, 5);
        Date start = calendar.getTime();

        calendar.add(Calendar.DATE, 7);
        Date end = calendar.getTime();

        order.setStartDate(start);
        order.setEndDate(end);

        calendar.add(Calendar.DATE, 5);
        car.setAvailableEndDate(calendar.getTime());


        Object[] isValid = orderService.validateOrder(car, order);
        assertEquals(true, isValid[0]);
        assertEquals(null, isValid[1]);
    }

    @Test
    void whenOrderIsNotOwners_failCancelOrder() {
        Order order = new Order();
        Customer customer = new Customer();
        customer.setId(1);
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2);

        order.setCustomer(customer);

        assertThrows(IllegalUserAccessException.class, () -> orderService.cancelOrder(anotherCustomer, order));

    }

    @Test
    void whenOrderMadeAWeekAgo_failCancelOrder() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date createdDate = calendar.getTime();

        Customer customer = new Customer();

        Order order = new Order();
        order.setCreatedDate(createdDate);
        order.setCustomer(customer);

        assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(customer, order));
    }

    @Test
    void whenAllIsWell_cancelOrder() {
        Customer customer = new Customer();
        Order order = new Order();

        order.setCustomer(customer);
        orderService.cancelOrder(customer, order);
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }
}