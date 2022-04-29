package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.CustomerService;
import com.mpp.gaskeun.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProviderService providerService;

    @GetMapping("/")
    public String displayHomePage(Model model) {
        return "index";
    }

    @PostMapping("/api/registration/customer")
    public @ResponseBody ResponseEntity<?> registerCustomer(@RequestBody Customer newCustomer) {
        customerService.register(newCustomer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/api/registration/provider")
    public @ResponseBody ResponseEntity<?> registerProvider(@RequestBody RentalProvider provider) {
        providerService.register(provider);
        return new ResponseEntity<>(provider, HttpStatus.CREATED);
    }

}
