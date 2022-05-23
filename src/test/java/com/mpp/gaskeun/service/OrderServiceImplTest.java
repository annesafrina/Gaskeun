package com.mpp.gaskeun.service;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.OrderStatus;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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


        boolean isOwner = orderService.verifyOrderOwnership(rentalProvider, order);

        assertTrue(isOwner);
    }

    @Test
    void whenOrderNullAndNotOwnedByProvider_mustReturnFalse() throws InvocationTargetException, IllegalAccessException {
        var rentalProvider = new RentalProvider();
        boolean isOwner = orderService.verifyOrderOwnership(rentalProvider, null);
        assertFalse(isOwner);
    }

    @Test
    void whenOrderNotNullAndNotOwnedByProvider_mustReturnFalse() throws InvocationTargetException, IllegalAccessException {
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
        var provider = new RentalProvider();
        var order = new Order();

        assertThrows(IllegalStateException.class, () -> {
            orderService.setOrderStatus(provider, order, status, bookingMessage);
        });
    }

    @Test
    void whenBookingMessageIsEmptyAndStatusRejected_throwsError() {
        String bookingMessage = "";
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        assertThrows(IllegalStateException.class, () -> {
            orderService.setOrderStatus(provider, order, status, bookingMessage);
        });
    }

    @Test
    void whenBookingMessageNotEmptyAndStatusRejected_savesNewStatusToRepository() {
        String bookingMessage = "Weeeee";
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.setOrderStatus(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageNullAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = null;
        var status = OrderStatus.WAITING_FOR_PAYMENT;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.setOrderStatus(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageEmptyAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = "";
        var status = OrderStatus.WAITING_FOR_PAYMENT;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.setOrderStatus(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(null, result.getBookingMessage());
    }

    @Test
    void whenBookingMessageNotEmptyAndStatusWaitingForPayment_savesStatusToRepository() {
        String bookingMessage = "Bwrelebek";
        var status = OrderStatus.REJECTED;
        var provider = new RentalProvider();
        var order = new Order();

        var result = orderService.setOrderStatus(provider, order, status, bookingMessage);

        verify(repository, times(1)).save(order);
        assertEquals(status, result.getOrderStatus());
        assertEquals(bookingMessage, result.getBookingMessage());
    }
}