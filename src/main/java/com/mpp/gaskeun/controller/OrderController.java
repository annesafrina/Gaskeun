package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create/{carId}")
    public String displayCreateOrder(Model model, @PathVariable("carId") String carId) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCarId(carId);

        model.addAttribute("orderDto", orderDto);
        return "create_order";
    }

    @PostMapping("/create")
    public String postCreateOrder(OrderDto orderDto, @AuthenticationPrincipal Customer customer, Model model) {

        log.info("Creating order");

        Order order;
        try {
            order = orderService.createOrder(customer, orderDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("orderDto", orderDto);
            return "create_order";
        }


        return "redirect:/order/display/" + order.getId();
    }

    @GetMapping("/display/{orderId}")
    public String displayIndividualOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, Model model) {
        Order order = orderService.getOrder(Long.parseLong(orderId), user);
        model.addAttribute("order", order);
        model.addAttribute("is-provider", user instanceof RentalProvider);
        return "order_details";
    }
}
