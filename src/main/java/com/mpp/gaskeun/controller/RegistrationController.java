package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CustomerDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.AuthenticationService;
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

        try {
            authenticationService.register(customerDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "customer_registration";
        }

        return "redirect:/registration/customer";
    }

    @GetMapping("/provider")
    public String registerProviderGet(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "provider_registration";
    }

    @PostMapping("/provider")
    public String registerProviderPost(@ModelAttribute UserDto providerDto, Model model) {

        try {
            authenticationService.register(providerDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "provider_registration";
        }

        return "redirect:/registration/provider";
    }
}

