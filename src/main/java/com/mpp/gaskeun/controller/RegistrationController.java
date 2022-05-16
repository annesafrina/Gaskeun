package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/customer")
    public String registerCustomer(Model model) {
        CustomerDto customerDto = new CustomerDto();
        model.addAttribute("customerDto", customerDto);
        return "customer_registration";
    }

    @PostMapping("/customer")
    public String registerCustomerPost(@ModelAttribute CustomerDto customerDto, Model model) {
        log.info(customerDto.getName());
        return "redirect:/registration/customer";
    }

}

