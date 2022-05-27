package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.ConfirmOrderDto;
import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.exception.IllegalUserAccessException;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.service.CarService;
import com.mpp.gaskeun.service.OrderService;
import com.mpp.gaskeun.utils.DateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.EnumMap;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {

    public static final String BASE_64_IMAGE = "base64Image";
    private final EnumMap<OrderStatus, String> cssStyleStatus = new EnumMap<>(OrderStatus.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private CarService carService;

    OrderController() {
        cssStyleStatus.put(OrderStatus.PENDING, "background-color: #ECE4B7;");
        cssStyleStatus.put(OrderStatus.ACTIVE, "background-color: #33CA7F; color: white;");
        cssStyleStatus.put(OrderStatus.WAITING_FOR_PAYMENT, "background-color: #7DCFB6;");
        cssStyleStatus.put(OrderStatus.CANCELLED, "background-color: #FC9F5B;");
        cssStyleStatus.put(OrderStatus.REJECTED, "background-color: #D50000; color: white;");
        cssStyleStatus.put(OrderStatus.COMPLETED, "background-color: #00CFC1; color: white;");
    }


    @GetMapping("/create/{carId}")
    public String displayCreateOrder(Model model, @PathVariable("carId") String carId) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCarId(carId);

        Car car = carService.getCarByIdAllowAnyone(Long.parseLong(carId));
        String base64String = car.getPicture();

        model.addAttribute("orderDto", orderDto);
        model.addAttribute("car", car);
        model.addAttribute(BASE_64_IMAGE, base64String);
        return "create_order";
    }

    @PostMapping("/create")
    public String postCreateOrder(OrderDto orderDto, @AuthenticationPrincipal UserDetails user, Model model) {
        log.info("Creating order");

        if (!(user instanceof Customer)) {
            return "redirect:/";
        }


        Order order;
        try {
            order = orderService.createOrder((Customer) user, orderDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            Car car = carService.getCarByIdAllowAnyone(Long.parseLong(orderDto.getCarId()));
            model.addAttribute("orderDto", orderDto);
            model.addAttribute(BASE_64_IMAGE, car.getPicture());
            model.addAttribute("car", car);
            return "create_order";
        }


        return "redirect:/order/display/" + order.getId();
    }

    @GetMapping("/display/{orderId}")
    public String displayIndividualOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, Model model) {
        Order order = orderService.getOrder(Long.parseLong(orderId), user);
        Car orderCar = order.getCar();

        String cssStyle = cssStyleStatus.get(order.getOrderStatus());
        String startDateString = DateParser.parse(order.getStartDate());
        String endDateString = DateParser.parse(order.getEndDate());

        model.addAttribute("order", order);
        model.addAttribute("car", orderCar);
        model.addAttribute(BASE_64_IMAGE, orderCar.getPicture());
        model.addAttribute("cssStyle", cssStyle);
        model.addAttribute("price", (order.getEndDate().getTime() - order.getStartDate().getTime()) / (1000 * 24 * 3600) * order.getCar().getPriceRate());

        model.addAttribute("startDate", startDateString);
        model.addAttribute("endDate", endDateString);

        model.addAttribute("isProvider", user instanceof RentalProvider);
        model.addAttribute("isRejected", order.getOrderStatus() == OrderStatus.REJECTED);
        model.addAttribute("isPending", order.getOrderStatus() == OrderStatus.PENDING);
        model.addAttribute("isWaitingForPayment", order.getOrderStatus() == OrderStatus.WAITING_FOR_PAYMENT);
        model.addAttribute("isActive", order.getOrderStatus() == OrderStatus.ACTIVE);
        model.addAttribute("orderStatusCleaned", order.getOrderStatus().toString().replaceAll("_", " "));
        return "order_details";
    }

    private ResponseEntity<Object> confirmOrRejectUtils(long id, UserDetails user, ConfirmOrderDto confirmOrderDto, OrderStatus status) {
        try {
            Order order = orderService.getOrder(id, user);
            orderService.setOrderStatus(order, status, confirmOrderDto.getBookingMessage());
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
    public ResponseEntity<Object> confirmOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), user, confirmOrderDto, OrderStatus.WAITING_FOR_PAYMENT);
    }

    @PostMapping(value = "/reject/{orderId}")
    public ResponseEntity<Object> rejectOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), user, confirmOrderDto, OrderStatus.REJECTED);
    }

    @PostMapping(value = "/pay/{orderId}")
    public ResponseEntity<Object> payOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), user, confirmOrderDto, OrderStatus.ACTIVE);
    }

    @PostMapping(value = "/complete/{orderId}")
    public ResponseEntity<Object> completeOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), user, confirmOrderDto, OrderStatus.COMPLETED);
    }

    @PostMapping(value = "/cancel/{orderId}")
    public ResponseEntity<Object> cancelOrder(@PathVariable("orderId") String orderId, @AuthenticationPrincipal UserDetails user, @RequestBody ConfirmOrderDto confirmOrderDto) {
        return confirmOrRejectUtils(Long.parseLong(orderId), user, confirmOrderDto, OrderStatus.CANCELLED);
    }

}
