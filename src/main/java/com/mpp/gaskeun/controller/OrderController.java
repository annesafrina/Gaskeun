package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.ConfirmOrderDto;
import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.exception.IllegalUserAccessException;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.OrderStatus;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create/{carId}")
    public String displayCreateOrder(Model model, @PathVariable("carId") String carId, @AuthenticationPrincipal Customer customer) {
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

        model.addAttribute("isProvider", user instanceof RentalProvider);
        model.addAttribute("isRejected", order.getOrderStatus() == OrderStatus.REJECTED);
        model.addAttribute("isPending", order.getOrderStatus() == OrderStatus.PENDING);
        model.addAttribute("isWaitingForPayment", order.getOrderStatus() == OrderStatus.WAITING_FOR_PAYMENT);
        model.addAttribute("isActive", order.getOrderStatus() == OrderStatus.ACTIVE);
        model.addAttribute("orderStatusCleaned", order.getOrderStatus().toString().replaceAll("_", " "));
        return "order_details";
    }

    private ResponseEntity<?> confirmOrRejectUtils(long id, RentalProvider provider, ConfirmOrderDto confirmOrderDto, OrderStatus status) {
       try {
           Order order = orderService.getOrder(id, provider);
           orderService.setOrderStatus(provider, order, status, confirmOrderDto.getBookingMessage());
           return ResponseEntity.ok(order);
       } catch (IllegalUserAccessException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
       } catch (NoSuchElementException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       } catch (IllegalStateException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }


    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal RentalProvider provider, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), provider, confirmOrderDto, OrderStatus.WAITING_FOR_PAYMENT);
    }

    @PostMapping(value = "/reject/{orderId}")
    public ResponseEntity<?> rejectOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal RentalProvider provider, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), provider, confirmOrderDto, OrderStatus.REJECTED);
    }

    @PostMapping(value = "/pay/{orderId}")
    public ResponseEntity<?> payOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal RentalProvider provider, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), provider, confirmOrderDto, OrderStatus.ACTIVE);
    }

    @PostMapping(value = "/complete/{orderId}")
    public ResponseEntity<?> completeOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal RentalProvider provider, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), provider, confirmOrderDto, OrderStatus.COMPLETED);
    }
}