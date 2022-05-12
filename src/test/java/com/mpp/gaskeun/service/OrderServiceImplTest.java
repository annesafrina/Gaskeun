package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.OrderStatus;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository repository;

    private Method publicVerifyOrderOwnership;

    @BeforeTestMethod()
    void setVisibility() throws NoSuchMethodException {
        Class<?>[] parameters = {RentalProvider.class, Order.class};
        publicVerifyOrderOwnership = orderService.getClass().getDeclaredMethod("verifyOrderOwnership", parameters);
        publicVerifyOrderOwnership.setAccessible(true);
    }

    @Test
    void whenOrderIsNotNullAndAssignedToProvider_mustReturnTrue() throws InvocationTargetException, IllegalAccessException {
        var rentalProvider = new RentalProvider();
        var car = new Car();
        var order = new Order();

        car.setRentalProvider(rentalProvider);
        order.setCar(car);

        boolean isOwner = (boolean) publicVerifyOrderOwnership.invoke(orderService, rentalProvider, order);

        assertTrue(isOwner);
    }

    @Test
    void whenOrderNullAndNotOwnedByProvider_mustReturnFalse() throws InvocationTargetException, IllegalAccessException {
        var rentalProvider = new RentalProvider();
        boolean isOwner = (boolean) publicVerifyOrderOwnership.invoke(orderService, rentalProvider, null);
        assertFalse(isOwner);
    }

    @Test
    void whenOrderNotNullAndNotOwnedByProvider_mustReturnFalse() throws InvocationTargetException, IllegalAccessException {
        var rentalProviderA = new RentalProvider();
        var rentalProviderB = new RentalProvider();
        var car = new Car();
        var order = new Order();

        car.setRentalProvider(rentalProviderA);
        order.setCar(car);

        boolean isOwner = (boolean) publicVerifyOrderOwnership.invoke(orderService, rentalProviderB, order);
        assertFalse(isOwner);
    }

    @Test
    void whenBookingMessageIsNullAndStatusRejected_shouldSaveObjectToDatabaseAndReturnCorrectly() {
        String bookingMessage = null;
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.confirmOrRejectOrder(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageIsEmptyAndStatusRejected() {
        String bookingMessage = "";
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.confirmOrRejectOrder(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageNotEmptyAndStatusRejected() {

    }

    @Test
    void whenBookingMessageNullAndStatusWaitingForPayment() {

    }

    @Test
    void whenBookingMessageEmptyAndStatusWaitingForPayment() {

    }

    @Test
    void whenBookingMessageNotEmptyAndStatusWaitingForPayment() {

    }
}