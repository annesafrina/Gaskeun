package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CarRepository;
import com.mpp.gaskeun.repository.CustomerRepository;
import com.mpp.gaskeun.repository.OrderRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer firstCustomer;

    private Customer secondCustomer;

    @BeforeEach
    void setup() {
        customerService = new CustomerServiceImpl(encoder, customerRepository, orderRepository);
    }

    @BeforeEach
    void setupFirstCustomer() {
        firstCustomer = new Customer();
        firstCustomer.setId(1);
        firstCustomer.setEmail("email@email.com");
        firstCustomer.setPassword("encryptedPassword");
        firstCustomer.setPhoneNumber("0122883");
        firstCustomer.setIdCardNumber("abc");
    }

    @BeforeEach
    void setupSecondProvider() {
        secondCustomer = new Customer();
        secondCustomer.setId(2);
        secondCustomer.setEmail("pesan@email.com");
    }

    @Test
    void whenRepoEmptyAndProviderDoesNotOwnAnyCar_mustReturn0() {
        when(orderRepository.findAll()).thenReturn(List.of());
        int expectedCount = 0;
        List<Order> findAllOrders= customerService.findAllOrders(firstCustomer);
        assertEquals(expectedCount, findAllOrders.size());
    }

    @Test
    void whenRepoNotEmptyAndCustomerDoesNotOwnAnyOrder_mustReturn0() {
        Order order1 = new Order();
        order1.setCustomer(secondCustomer);
        Order order2 = new Order();
        order2.setCustomer(secondCustomer);
        List<Order> orderInRepo = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(orderInRepo);
        int expectedCount = 0;
        List<Order> findAllOrders = customerService.findAllOrders(firstCustomer);
        assertEquals(expectedCount, findAllOrders.size());
    }

    @Test
    void whenRepoNotEmptyAndCustomerOwnSomeOrders_mustReturnProviderCarCount() {
        Order order1 = new Order();
        order1.setCustomer(firstCustomer);
        Order order2 = new Order();
        order2.setCustomer(secondCustomer);
        Order order3 = new Order();
        order3.setCustomer(secondCustomer);

        List<Order> orderInRepo = List.of(order1, order2, order3);
        when(orderRepository.findAll()).thenReturn(orderInRepo);

        int expectedCount = 1;
        List<Order> findAllOrders = customerService.findAllOrders(firstCustomer);

        assertEquals(expectedCount, findAllOrders.size());
    }

    @Test
    void whenUserDtoNotValid_mustThrowIncompleteFormException() {
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setName("");
        assertThrows(IncompleteFormException.class, () -> customerService.update(firstCustomer, invalidUserDto));
    }

    @Test
    void whenUserDtoValidButDoesNotContainPassword_mustCallSaveAndUpdateCustomer() {
        UserDto validUserDto = new UserDto();
        String newName = "New Name";
        String phoneNumber = "0812345678";
        String oldPassword = firstCustomer.getPassword();

        validUserDto.setName(newName);
        validUserDto.setPhoneNumber(phoneNumber);

        customerService.update(firstCustomer, validUserDto);

        verify(customerRepository, times(1)).save(firstCustomer);
        assertEquals(newName, firstCustomer.getName());
        assertEquals(phoneNumber, firstCustomer.getPhoneNumber());
        assertEquals(oldPassword, firstCustomer.getPassword());
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

        when(encoder.matches(validUserDto.getOldPassword(), firstCustomer.getPassword())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> customerService.update(firstCustomer, validUserDto));
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

        when(encoder.matches(validUserDto.getOldPassword(), firstCustomer.getPassword())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> customerService.update(firstCustomer, validUserDto));
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

        when(encoder.matches(validUserDto.getOldPassword(), firstCustomer.getPassword())).thenReturn(true);
        when(encoder.encode(newPassword)).thenReturn(encryptedNewPassword);

        customerService.update(firstCustomer, validUserDto);

        verify(customerRepository, times(1)).save(firstCustomer);
        assertEquals(newName, firstCustomer.getName());
        assertEquals(phoneNumber, firstCustomer.getPhoneNumber());
        assertEquals(encryptedNewPassword, firstCustomer.getPassword());
    }

}