package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    public static final String CUSTOMER = "customer";
    public static final String ERROR = "error";

    @Autowired
    private CustomerService customerService;

    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal Customer customer, Model model) {
        String email = customer.getEmail();

        model.addAttribute("type", CUSTOMER);

        return "order-list";
    }


    @GetMapping("/info")
    public String getProviderInfo(HttpServletRequest request, @AuthenticationPrincipal UserDetails user, Model model) {
        if (!(user instanceof Customer customer)) {
            return "redirect:/";
        }

        Map<String, ?> errorFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (errorFlashMap == null ) {
            errorFlashMap = new HashMap<>();
        }

        UserDto userDto = new UserDto();
        userDto.fillCustomerDto(customer);
        model.addAttribute("error", errorFlashMap.get(ERROR));
        model.addAttribute("type", CUSTOMER);
        model.addAttribute("user", userDto);
        model.addAttribute("idCard", customer.getIdCardNumber());
        model.addAttribute("drivingLicense", customer.getDrivingLicenseNumber());
        return "user_profile";
    }

    @PostMapping("/info")
    public String updateProviderInfoPost(@AuthenticationPrincipal UserDetails user,
                                         @ModelAttribute UserDto userDto,
                                         RedirectAttributes attrs
    ) {

        if (!(user instanceof Customer customer)) {
            return "redirect:/";
        }

        try {
            customerService.update(customer, userDto);
        } catch (IllegalArgumentException e) {
            attrs.addFlashAttribute("error",e.getMessage());
            return "redirect:/customer/info";
        }

        return "redirect:/customer/info";
    }


    @GetMapping("/api/all-orders")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        if (user instanceof Customer customer) {
            List<OrderDisplayDto> orderByCustomer = customerService.findAllOrdersInDto(customer);
            response.put("type", CUSTOMER);
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
            List<OrderDisplayDto> orderByCustomer = customerService.findAllOrdersInDto(customer);
            response.put("type", CUSTOMER);
            response.put("data", orderByCustomer);

        } else if (user instanceof RentalProvider) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }
}
