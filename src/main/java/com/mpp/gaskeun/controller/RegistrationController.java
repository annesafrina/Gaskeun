package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CustomerDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.AuthenticationService;
import com.mpp.gaskeun.service.CustomerService;
import com.mpp.gaskeun.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/")
    public String chooseRegistration() {
        return "choose_registration";
    }

    @GetMapping("/customer")
    public String registerCustomer(Model model) {
        CustomerDto customerDto = new CustomerDto();
        model.addAttribute("customerDto", customerDto);
        return "customer_registration";
    }

    @PostMapping("/customer")
    public String registerCustomerPost(@ModelAttribute CustomerDto customerDto, Model model) {
        if(!customerDto.getPassword().equals(customerDto.getPasswordConfirmation())) {
            model.addAttribute("error", "Password does not match");
            return "customer_registration";
        }

        Customer newCustomer = new Customer();
        newCustomer.setEmail(customerDto.getEmail());
        newCustomer.setPassword(customerDto.getPassword());
        newCustomer.setDrivingLicenseNumber(customerDto.getDriving_license());
        newCustomer.setIdCardNumber(customerDto.getId_card());
        newCustomer.setName(customerDto.getName());
        newCustomer.setPhoneNumber(customerDto.getPhone_number());

        try {
            authenticationService.register(newCustomer);

        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "customer_registration";
        }

        return "redirect:/registration/customer";
    }

    @GetMapping("/provider")
    public String registerProviderGet(Model model) {
        UserDto providerDto = new UserDto();
        model.addAttribute("providerDto", providerDto);
        return "provider_registration";
    }

    @PostMapping("/provider")
    public String registerProviderPost(@ModelAttribute UserDto providerDto, Model model) {
        if(!providerDto.getPassword().equals(providerDto.getPasswordConfirmation())) {
            model.addAttribute("error", "Password does not match");
            return "customer_registration";
        }

        RentalProvider newProvider = new RentalProvider();
        newProvider.setEmail(providerDto.getEmail());
        newProvider.setName(providerDto.getName());
        newProvider.setPassword(providerDto.getPassword());

        try {
            authenticationService.register(newProvider);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "provider_registration";
        }

        return "redirect:/registration/provider";
    }
}

