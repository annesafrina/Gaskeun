package com.mpp.gaskeun.service;

import com.mpp.gaskeun.exception.OrderDoesNotExistException;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setup() {

    }

    @Test
    void whenOrderWithIdNotExist_mustThrowOrderDoesNotExist() throws NoSuchMethodException {
        Method pubValidateOrderExist = reviewService.getClass().getDeclaredMethod("validateOrderExists", Long.class);
        pubValidateOrderExist.setAccessible(true);
        long id = 3;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                () -> pubValidateOrderExist.invoke(reviewService, id));

        assert exception.getCause() instanceof OrderDoesNotExistException;
    }

    @Test
    void whenOrderWithIdExist_mustReturnOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method pubValidateOrderExist = reviewService.getClass().getDeclaredMethod("validateOrderExists", Long.class);
        pubValidateOrderExist.setAccessible(true);
        long id = 3;
        Order dummyOrder = new Order();
        dummyOrder.setId(id);
        when(orderRepository.findById(id)).thenReturn(Optional.of(dummyOrder));

        Order returnedOrder = (Order) pubValidateOrderExist.invoke(reviewService, id);

        assertEquals(id, returnedOrder.getId());
    }

    @Test
    void whenOrderNotOwnedByUser_mustReturnFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method pubOrderOwnedByUser = reviewService.getClass().getDeclaredMethod("orderOwnedByUser", Order.class, UserDetails.class);
        pubOrderOwnedByUser.setAccessible(true);
        Customer owningCustomer = new Customer();
        owningCustomer.setId(320);

        Customer notOwningCustomer = new Customer();
        notOwningCustomer.setId(3);

        Order order = new Order();
        order.setCustomer(owningCustomer);

        boolean orderIsOwnByUser = (boolean) pubOrderOwnedByUser.invoke(reviewService, order, notOwningCustomer);
        assertFalse(orderIsOwnByUser);
    }

    @Test
    void whenOrderOwnedByCustomer_mustReturnTrue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method pubOrderOwnedByUser = reviewService.getClass().getDeclaredMethod("orderOwnedByUser", Order.class, UserDetails.class);
        pubOrderOwnedByUser.setAccessible(true);
        Customer owningCustomer = new Customer();
        owningCustomer.setId(320);

        Order order = new Order();
        order.setCustomer(owningCustomer);

        boolean orderIsOwnByUser = (boolean) pubOrderOwnedByUser.invoke(reviewService, order, owningCustomer);
        assertTrue(orderIsOwnByUser);
    }

    void whenOrderOwnedByProvider_mustReturnTrue() throws NoSuchMethodException {
        Method pubOrderOwnedByUser = reviewService.getClass().getDeclaredMethod("orderOwnedByUser", Order.class, UserDetails.class);
        pubOrderOwnedByUser.setAccessible(true);
        RentalProvider owningProvider = new RentalProvider();
        owningProvider.setId(320);

        Order order = new Order();
    }
}