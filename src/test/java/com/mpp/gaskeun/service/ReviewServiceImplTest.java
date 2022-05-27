package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.ReviewDto;
import com.mpp.gaskeun.exception.OrderDoesNotExistException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.repository.ReviewRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

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

        boolean orderIsOwnedByUser = (boolean) pubOrderOwnedByUser.invoke(reviewService, order, owningCustomer);
        assertTrue(orderIsOwnedByUser);
    }

    @Test
    void whenOrderOwnedByProvider_mustReturnTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method pubOrderOwnedByUser = reviewService.getClass().getDeclaredMethod("orderOwnedByUser", Order.class, UserDetails.class);
        pubOrderOwnedByUser.setAccessible(true);
        RentalProvider owningProvider = new RentalProvider();
        owningProvider.setId(320);

        Car car = new Car();
        car.setRentalProvider(owningProvider);

        Order order = new Order();
        order.setCar(car);

        boolean orderIsOwnedByUser = (boolean) pubOrderOwnedByUser.invoke(reviewService, order, owningProvider);
        assertTrue(orderIsOwnedByUser);
    }

    @Test
    void whenOrderIsCompleted_mustReturnTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method pubValidateOrder = reviewService.getClass().getDeclaredMethod("validateOrderIsCompleted", Order.class);
        pubValidateOrder.setAccessible(true);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETED);

        boolean isComplete = (boolean) pubValidateOrder.invoke(reviewService, order);
        assertTrue(isComplete);
    }

    @Test
    void whenOrderIsNotCompleted_mustReturnFalse() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method pubValidateOrder = reviewService.getClass().getDeclaredMethod("validateOrderIsCompleted", Order.class);
        pubValidateOrder.setAccessible(true);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);

        boolean isComplete = (boolean) pubValidateOrder.invoke(reviewService, order);
        assertFalse(isComplete);
    }

    @Test
    void whenCarReviewIsSubmitted_mustBeSave() {
        ReviewDto reviewDto = new ReviewDto();
        Order dummyOrder = new Order();
        Car dummyCar = new Car();
        dummyCar.setRating(0);
        dummyCar.setNumberOfReviews(0);
        dummyOrder.setId(1);
        dummyOrder.setCar(dummyCar);
        reviewDto.setReviewType(ReviewType.CAR);
        reviewDto.setOrderId("1");
        reviewDto.setRating(5);
        reviewDto.setDescription("This is my Car review");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(dummyOrder));

        Review carReview = reviewService.submitReview(reviewDto);

        verify(reviewRepository, times(1)).save(carReview);
        assertTrue(carReview instanceof CarReview);
    }

    @Test
    void whenProviderReviewIsSubmitted_mustBeSave() {
        ReviewDto reviewDto = new ReviewDto();
        Order dummyOrder = new Order();
        Car dummyCar = new Car();
        RentalProvider dummyProvider = new RentalProvider();
        dummyProvider.setPerformanceRating(0);
        dummyProvider.setNumberOfReviews(0);
        dummyCar.setRentalProvider(dummyProvider);
        dummyOrder.setId(1);
        dummyOrder.setCar(dummyCar);
        reviewDto.setReviewType(ReviewType.PROVIDER);
        reviewDto.setOrderId("1");
        reviewDto.setRating(5);
        reviewDto.setDescription("This is my Provider review");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(dummyOrder));

        Review providerReview = reviewService.submitReview(reviewDto);

        verify(reviewRepository, times(1)).save(providerReview);
        assertTrue(providerReview instanceof ProviderReview);
    }

    @Test
    void whenCustomerReviewIsSubmitted_mustBeSave() {
        ReviewDto reviewDto = new ReviewDto();
        Order dummyOrder = new Order();
        Car dummyCar = new Car();
        Customer dummyCustomer = new Customer();
        dummyCustomer.setRating(5);
        dummyCustomer.setNumberOfReviews(0);
        dummyOrder.setId(1);
        dummyOrder.setCar(dummyCar);
        dummyOrder.setCustomer(dummyCustomer);
        reviewDto.setReviewType(ReviewType.CUSTOMER);
        reviewDto.setOrderId("1");
        reviewDto.setRating(5);
        reviewDto.setDescription("This is my Customer review");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(dummyOrder));

        Review customerReview = reviewService.submitReview(reviewDto);

        verify(reviewRepository, times(1)).save(customerReview);
        assertTrue(customerReview instanceof CustomerReview);
    }
}