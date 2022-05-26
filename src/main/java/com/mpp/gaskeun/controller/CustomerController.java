package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.CustomerService;
import com.mpp.gaskeun.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal Customer customer, Model model) {
        String email = customer.getEmail();

        model.addAttribute("type", "customer");

        return "order-list";
    }


    @GetMapping("/api/all-orders")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        if (user instanceof Customer customer) {
            List<OrderDisplayDto> orderByCustomer = customerService.findAllOrders(customer).stream().map((order) ->
                    OrderUtils.lightDisplayOrder(order)).toList();
            response.put("type", "customer");
            response.put("data", orderByCustomer);

        } else if (user instanceof RentalProvider) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/current-orders")
    public ResponseEntity<?> getCurrentOrders(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        if (user instanceof Customer customer) {
            List<OrderDisplayDto> orderByCustomer = customerService.findAllOnGoingOrders(customer).stream().map((order) ->
                    OrderUtils.lightDisplayOrder(order)).toList();
            response.put("type", "customer");
            response.put("data", orderByCustomer);

        } else if (user instanceof RentalProvider) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }
}
