package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.OrderStatus;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {
    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private RentalProvider firstProvider;

    private RentalProvider secondProvider;

    @BeforeEach
    void setup() {
        providerService = new ProviderServiceImpl(encoder, providerRepository, carRepository, orderRepository);
    }

    @BeforeEach
    void setupFirstProvider() {
        firstProvider = new RentalProvider();
        firstProvider.setId(1);
        firstProvider.setEmail("email@email.com");
        firstProvider.setPassword("encryptedPassword");
        firstProvider.setPhoneNumber("0122883");
        firstProvider.setPerformanceRating(5);
    }

    @BeforeEach
    void setupSecondProvider() {
        secondProvider = new RentalProvider();
        secondProvider.setId(2);
        secondProvider.setEmail("pesan@email.com");
    }

    @Test
    void whenRepoEmptyAndProviderDoesNotOwnAnyCar_mustReturn0() {
        when(carRepository.findAll()).thenReturn(List.of());
        int expectedCount = 0;
        long numberOfCarRegistered = providerService.getNumberOfCarRegistered(firstProvider);
        assertEquals(expectedCount, numberOfCarRegistered);
    }

    @Test
    void whenRepoNotEmptyAndProviderDoesNotOwnAnyCar_mustReturn0() {
        Car car1 = new Car();
        car1.setRentalProvider(secondProvider);
        Car car2 = new Car();
        car2.setRentalProvider(secondProvider);
        List<Car> carInRepo = List.of(car1, car2);

        when(carRepository.findAll()).thenReturn(carInRepo);
        int expectedCount = 0;
        long numberOfCarsRegistered = providerService.getNumberOfCarRegistered(firstProvider);
        assertEquals(expectedCount, numberOfCarsRegistered);
    }

    @Test
    void whenRepoNotEmptyAndProviderOwnSomeCars_mustReturnProviderCarCount() {
        Car car1 = new Car();
        car1.setRentalProvider(firstProvider);
        Car car2 = new Car();
        car2.setRentalProvider(secondProvider);
        Car car3 = new Car();
        car3.setRentalProvider(secondProvider);

        List<Car> carInRepo = List.of(car1, car2, car3);
        when(carRepository.findAll()).thenReturn(carInRepo);

        int expectedCount = 1;
        long numberOfRegistered = providerService.getNumberOfCarRegistered(firstProvider);

        assertEquals(expectedCount, numberOfRegistered);
    }

    @Test
    void whenUserDtoNotValid_mustThrowIncompleteFormException() {
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setName("");
        assertThrows(IncompleteFormException.class, () -> providerService.update(firstProvider, invalidUserDto));
    }

    @Test
    void whenUserDtoValidButDoesNotContainPassword_mustCallSaveAndUpdateProvider() {
        UserDto validUserDto = new UserDto();
        String newName = "New Name";
        String phoneNumber = "0812345678";
        String oldPassword = firstProvider.getPassword();

        validUserDto.setName(newName);
        validUserDto.setPhoneNumber(phoneNumber);

        providerService.update(firstProvider, validUserDto);

        verify(providerRepository, times(1)).save(firstProvider);
        assertEquals(newName, firstProvider.getName());
        assertEquals(phoneNumber, firstProvider.getPhoneNumber());
        assertEquals(oldPassword, firstProvider.getPassword());
    }

    @Test
    void whenUserDtoValidAndContainPasswordButDoesNotMatchOld_mustThrowIllegalArgumentException() {
        UserDto validUserDto = new UserDto();
        String newName = "New Name";
        String phoneNumber = "0812345678";
        String oldPasswordPlain = "password";
        String newPassword = "password123";
        String newPasswordConfirmation = "password123";

        validUserDto.setName(newName);
        validUserDto.setPhoneNumber(phoneNumber);
        validUserDto.setOldPassword(oldPasswordPlain);
        validUserDto.setPassword(newPassword);
        validUserDto.setPasswordConfirmation(newPasswordConfirmation);

        when(encoder.matches(validUserDto.getOldPassword(), firstProvider.getPassword())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> providerService.update(firstProvider, validUserDto));
    }

    @Test
    void whenUserDtoValidAndContainPasswordButPasswordAndConfirmationDoesNotMatch_mustThrowIllegalArgumentException() {
        UserDto validUserDto = new UserDto();
        String newName = "New Name";
        String phoneNumber = "0812345678";
        String oldPasswordPlain = "unencryptedPassword";
        String newPassword = "password123";
        String newPasswordConfirmation = "differentPassword";

        validUserDto.setName(newName);
        validUserDto.setPhoneNumber(phoneNumber);
        validUserDto.setOldPassword(oldPasswordPlain);
        validUserDto.setPassword(newPassword);
        validUserDto.setPasswordConfirmation(newPasswordConfirmation);

        when(encoder.matches(validUserDto.getOldPassword(), firstProvider.getPassword())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> providerService.update(firstProvider, validUserDto));
    }

    @Test
    void whenUserDtoValidAndContainPasswordAndAllMatch_mustCallSaveAndUpdate() {
        UserDto validUserDto = new UserDto();
        String newName = "New Name";
        String phoneNumber = "0812345678";
        String oldPasswordPlain = "unencryptedPassword";
        String newPassword = "password123";
        String newPasswordConfirmation = "password123";
        String encryptedNewPassword = "encryptedNewPassword";

        validUserDto.setName(newName);
        validUserDto.setPhoneNumber(phoneNumber);
        validUserDto.setOldPassword(oldPasswordPlain);
        validUserDto.setPassword(newPassword);
        validUserDto.setPasswordConfirmation(newPasswordConfirmation);

        when(encoder.matches(validUserDto.getOldPassword(), firstProvider.getPassword())).thenReturn(true);
        when(encoder.encode(newPassword)).thenReturn(encryptedNewPassword);

        providerService.update(firstProvider, validUserDto);

        verify(providerRepository, times(1)).save(firstProvider);
        assertEquals(newName, firstProvider.getName());
        assertEquals(phoneNumber, firstProvider.getPhoneNumber());
        assertEquals(encryptedNewPassword, firstProvider.getPassword());
    }

    @Test
    void whenProviderHasOrders_mustReturnAllOrders() {
        RentalProvider provider = new RentalProvider();
        provider.setEmail("provideremail");
        provider.setId(1);
        RentalProvider anotherProvider = new RentalProvider();
        provider.setEmail("anotherprovider");
        provider.setId(2);

        Car providerCar1 = new Car();
        Car providerCar2 = new Car();
        Car anotherCar = new Car();
        providerCar1.setRentalProvider(provider);
        providerCar2.setRentalProvider(provider);
        anotherCar.setRentalProvider(anotherProvider);

        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        order1.setCar(providerCar1);
        order2.setCar(providerCar2);
        order3.setCar(anotherCar);

        List<Order> allOrders = List.of(order1, order2, order3);
        List<Order> providersOrders = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);
        List<Order> retrievedOrders = providerService.findAllOrders(provider);

        assertEquals(providersOrders, retrievedOrders);
    }

    @Test
    void whenProviderHasNoOrders_mustReturnEmptyList() {
        RentalProvider provider = new RentalProvider();
        provider.setEmail("provideremail");
        provider.setId(1);
        RentalProvider anotherProvider = new RentalProvider();
        provider.setEmail("anotherprovider");
        provider.setId(2);

        Car anotherCar = new Car();
        anotherCar.setRentalProvider(anotherProvider);

        Order order = new Order();
        order.setCar(anotherCar);

        List<Order> allOrders = List.of(order);
        List<Order> providersOrders = List.of();

        when(orderRepository.findAll()).thenReturn(allOrders);
        List<Order> retrievedOrders = providerService.findAllOrders(provider);

        assertEquals(providersOrders, retrievedOrders);
    }

    @Test
    void whenFindAllOnGoingOrders_mustReturnOnlyOnGoingOrders() {
        RentalProvider provider = new RentalProvider();
        provider.setEmail("provideremail");
        provider.setId(1);
        RentalProvider anotherProvider = new RentalProvider();
        provider.setEmail("anotherprovider");
        provider.setId(2);

        Car providerCar1 = new Car();
        Car providerCar2 = new Car();
        Car providerCar3 = new Car();
        Car anotherCar = new Car();
        providerCar1.setRentalProvider(provider);
        providerCar2.setRentalProvider(provider);
        providerCar3.setRentalProvider(provider);
        anotherCar.setRentalProvider(anotherProvider);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();

        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();
        order1.setCar(providerCar1);
        order1.setOrderStatus(OrderStatus.ACTIVE);
        order1.setEndDate(tomorrow);
        order2.setCar(providerCar2);
        order2.setOrderStatus(OrderStatus.COMPLETED);
        order2.setEndDate(today);
        order3.setCar(anotherCar);
        order3.setOrderStatus(OrderStatus.PENDING);
        order3.setEndDate(today);
        order4.setCar(providerCar3);
        order4.setOrderStatus(OrderStatus.ACTIVE);
        order4.setEndDate(today);

        List<Order> allOrders = List.of(order1, order2, order3, order4);
        List<Order> providerActiveOrders = List.of(order1);

        when(orderRepository.findAll()).thenReturn(allOrders);
        List<Order> retrievedOrders = providerService.findAllOnGoingOrders(provider);

        assertEquals(providerActiveOrders, retrievedOrders);
    }


}