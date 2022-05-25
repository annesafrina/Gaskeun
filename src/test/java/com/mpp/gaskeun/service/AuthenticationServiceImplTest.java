package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.CustomerDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.exception.IncompleteFormException;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.repository.CustomerRepository;
import com.mpp.gaskeun.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder mockEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setup() {
        authenticationService = new AuthenticationServiceImpl(customerRepository, providerRepository, mockEncoder);
    }

    @Test
    void whenNoUserInRepositoryHasUsername_mustThrowUsernameNotFoundException() {
        String username = "username";
        when(customerRepository.findByEmail(username)).thenReturn(Optional.empty());
        when(providerRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(username));
    }

    @Test
    void whenCustomerHasUsername_mustReturnCustomerWithMatchingUsername() {
        String username = "username";
        Customer customer = new Customer();
        customer.setEmail(username);

        when(customerRepository.findByEmail(username)).thenReturn(Optional.of(customer));

        UserDetails retrievedUser = authenticationService.loadUserByUsername(username);
        assertEquals(username, retrievedUser.getUsername());
        assert retrievedUser instanceof Customer;
    }

    @Test
    void whenProviderHasUsername_mustReturnProviderWithMatchingUsername() {
        String username = "username";
        RentalProvider provider = new RentalProvider();
        provider.setEmail(username);

        when(customerRepository.findByEmail(username)).thenReturn(Optional.empty());
        when(providerRepository.findByEmail(username)).thenReturn(Optional.of(provider));

        UserDetails retrievedUser = authenticationService.loadUserByUsername(username);

        assertEquals(username, retrievedUser.getUsername());
        assert retrievedUser instanceof RentalProvider;
    }

    @Test
    void whenDtoNotComplete_mustThrowIncompleteFormException() {
        UserDto invalidUserDto = new UserDto();
        assertThrows(IncompleteFormException.class, () -> authenticationService.register(invalidUserDto));
    }

    @Test
    void whenUserDtoCompleteButPasswordDoesNotMatch_mustThrowIllegalArgumentException() {
        UserDto validUserDto = new UserDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password2");
        validUserDto.setPhoneNumber("08123456789");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(validUserDto));
        assertEquals("Password does not match confirmation", exception.getMessage());
    }

    @Test
    void whenUserDtoCompleteAndPasswordMatchButAlreadyInRepo_mustThrowIllegalArgumentException() {
        UserDto validUserDto = new UserDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password1");
        validUserDto.setPhoneNumber("08123456789");

        when(providerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.of(new RentalProvider()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> authenticationService.register(validUserDto));

        assertEquals(String.format("Email %s already exists", validUserDto.getEmail()), exception.getMessage());
    }

    @Test
    void whenUserDtoCompleteAndPasswordMatch_mustSaveAndReturnCustomer() {
        UserDto validUserDto = new UserDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password1");
        validUserDto.setPhoneNumber("08123456789");

        when(customerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.empty());
        when(providerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.empty());
        when(mockEncoder.encode(validUserDto.getPassword())).thenReturn(encoder.encode(validUserDto.getPassword()));

        RentalProvider savedUser = (RentalProvider) authenticationService.register(validUserDto);
        verify(providerRepository, times(1)).save(savedUser);

        assertEquals(validUserDto.getEmail(), savedUser.getEmail());
        assertEquals(validUserDto.getName(), savedUser.getName());
        assertEquals(validUserDto.getPhoneNumber(), savedUser.getPhoneNumber());

        System.out.println();
        assert encoder.matches(validUserDto.getPassword(), savedUser.getPassword());
    }

    @Test
    void whenCustomerDtoNotComplete_mustThrowIncompleteFormException() {
        UserDto invalidUserDto = new CustomerDto();
        assertThrows(IncompleteFormException.class, () -> authenticationService.register(invalidUserDto));
    }

    @Test
    void whenCustomerDtoCompleteButPasswordDoesNotMatch_mustThrowIllegalArgumentException() {
        CustomerDto validUserDto = new CustomerDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password2");
        validUserDto.setPhoneNumber("08123456789");
        validUserDto.setIdCard("ID_CARD");
        validUserDto.setDrivingLicense("DRIVING_LICENSE");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(validUserDto));

        assertEquals("Password does not match confirmation", exception.getMessage());
    }

    @Test
    void whenCustomerDtoCompleteAndPasswordMatchButAlreadyInRepo_mustThrowIllegalArgumentException() {
        CustomerDto validUserDto = new CustomerDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password1");
        validUserDto.setPhoneNumber("08123456789");
        validUserDto.setIdCard("ID_CARD");
        validUserDto.setDrivingLicense("DRIVING_LICENSE");


        when(customerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.of(new Customer()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.register(validUserDto)
        );

        assertEquals(String.format("Email %s already exists", validUserDto.getEmail()), exception.getMessage());
    }

    @Test
    void whenCustomerDtoCompleteAndPasswordMatch_mustSaveAndReturnCustomer() {
        CustomerDto validUserDto = new CustomerDto();
        validUserDto.setEmail("email");
        validUserDto.setName("name");
        validUserDto.setPassword("password1");
        validUserDto.setPasswordConfirmation("password1");
        validUserDto.setPhoneNumber("08123456789");
        validUserDto.setIdCard("ID_CARD");
        validUserDto.setDrivingLicense("DRIVING_LICENSE");

        when(customerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.empty());
        when(providerRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.empty());
        when(mockEncoder.encode(validUserDto.getPassword())).thenReturn(encoder.encode(validUserDto.getPassword()));

        Customer savedUser = (Customer) authenticationService.register(validUserDto);
        verify(customerRepository, times(1)).save(savedUser);

        assertEquals(validUserDto.getEmail(), savedUser.getEmail());
        assertEquals(validUserDto.getName(), savedUser.getName());
        assertEquals(validUserDto.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(validUserDto.getDrivingLicense(), savedUser.getDrivingLicenseNumber());
        assertEquals(validUserDto.getIdCard(), savedUser.getIdCardNumber());

        assert encoder.matches(validUserDto.getPassword(), savedUser.getPassword());
    }


}