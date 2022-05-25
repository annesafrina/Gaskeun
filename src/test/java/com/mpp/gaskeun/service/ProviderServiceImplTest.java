package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CarRepository;
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
class ProviderServiceImplTest {
    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private RentalProvider firstProvider;

    private RentalProvider secondProvider;

    @BeforeEach
    void setup() {
        providerService = new ProviderServiceImpl(providerRepository, carRepository, encoder);
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
        validUserDto.setPhone_number(phoneNumber);

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
        validUserDto.setPhone_number(phoneNumber);
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
        validUserDto.setPhone_number(phoneNumber);
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
        validUserDto.setPhone_number(phoneNumber);
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

}