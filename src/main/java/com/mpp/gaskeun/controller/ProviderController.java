package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Color;
import com.mpp.gaskeun.model.RentalProvider;
import com.mpp.gaskeun.model.Transmission;
import com.mpp.gaskeun.service.CarService;
import com.mpp.gaskeun.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/provider")
@Slf4j
public class ProviderController {

    public static final String REDIRECT_URL = "redirect:/";
    public static final String ERROR_ATTRIB_NAME = "error";
    public static final String CAR_DTO_ATTRIB_NAME = "carDto";
    public static final String PROVIDER_ATTRIB_VALUE = "provider";
    @Autowired
    private CarService carService;

    @Autowired
    private ProviderService providerService;

    @GetMapping("/info")
    public String getProviderInfo(HttpServletRequest request,
                                  @AuthenticationPrincipal UserDetails user,
                                  Model model
    ) {
        if (!(user instanceof RentalProvider provider)) {
            return REDIRECT_URL;
        }

        Map<String, ?> errorFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (errorFlashMap == null) {
            errorFlashMap = new HashMap<>();
        }

        UserDto userDto = new UserDto();
        userDto.fillDto(provider);
        model.addAttribute(ERROR_ATTRIB_NAME, errorFlashMap.get(ERROR_ATTRIB_NAME));
        model.addAttribute("type", PROVIDER_ATTRIB_VALUE);
        model.addAttribute("user", userDto);
        model.addAttribute("performanceRating", provider.getPerformanceRating());
        model.addAttribute("numCarsOwned", providerService.getNumberOfCarRegistered((RentalProvider) user));
        return "user_profile";
    }

    @PostMapping("/info")
    public String updateProviderInfoPost(@AuthenticationPrincipal UserDetails user,
                                         @ModelAttribute UserDto userDto,
                                         RedirectAttributes attrs
    ) {

        if (!(user instanceof RentalProvider)) {
            return REDIRECT_URL;
        }

        try {
            providerService.update((RentalProvider) user, userDto);
        } catch (IllegalArgumentException e) {
            attrs.addFlashAttribute(ERROR_ATTRIB_NAME, e.getMessage());
        }

        return "redirect:/provider/info";
    }

    @GetMapping("/register-car")
    public String registerCar(@AuthenticationPrincipal UserDetails user, Model model) {

        if (!(user instanceof RentalProvider))
            return REDIRECT_URL;

        CarDto dto = new CarDto();
        model.addAllAttributes(Map.of(
                CAR_DTO_ATTRIB_NAME, dto,
                "colors", Color.values(),
                "transmissions", Transmission.values(),
                "locations", carService.getAllLocations()
        ));

        return "car_registration";
    }

    @PostMapping("/register-car")
    public String registerCarPost(@AuthenticationPrincipal UserDetails user,
                                  @ModelAttribute CarDto carDto,
                                  @RequestParam("car-image") MultipartFile imageFile,
                                  Model model) throws IOException, ParseException {

        try {
            carDto.setImage(imageFile);
            carService.addCar((RentalProvider) user, carDto);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
        }

        return "redirect:/provider/cars";
    }

    @GetMapping("/update-car/{carId}")
    public String updateCar(
            @PathVariable("carId") long carId,
            @AuthenticationPrincipal UserDetails provider,
            Model model) {

        if (!(provider instanceof RentalProvider))
            return REDIRECT_URL;

        CarDto carDto = new CarDto();

        try {
            Car car = carService.getCarById((RentalProvider) provider, carId);
            carDto.fillDto(car);
            model.addAllAttributes(Map.of(
                    CAR_DTO_ATTRIB_NAME, carDto,
                    "colors", Color.values(),
                    "transmissions", Transmission.values(),
                    "locations", carService.getAllLocations()
            ));
            log.info("Car found {}", car.getLicensePlate());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
        }

        return "car_edit_listing";
    }


    @PostMapping("/update-car")
    public String updateCarPost(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("car-image") MultipartFile imageFile,
            @ModelAttribute CarDto carDto,
            Model model) {

        if (!(user instanceof RentalProvider))
            return REDIRECT_URL;

        try {
            log.info("Received car with license {}", carDto.getLicensePlate());
            carDto.setImage(imageFile);
            carService.updateCar((RentalProvider) user, carDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
            model.addAttribute(CAR_DTO_ATTRIB_NAME, carDto);
            model.addAllAttributes(Map.of(
                    CAR_DTO_ATTRIB_NAME, carDto,
                    "colors", Color.values(),
                    "transmissions", Transmission.values(),
                    "locations", carService.getAllLocations()
            ));
            return "car_edit_listing";
        }

        return "redirect:/provider/car-detail/" + carDto.getLicensePlate();
    }

    @GetMapping("/car-detail/{licensePlate}")
    public String displayCar(
            @PathVariable("licensePlate") String licensePlate,
            @AuthenticationPrincipal UserDetails user,
            Model model) {

        if (!(user instanceof RentalProvider))
            return REDIRECT_URL;

        try {
            Car car = carService.getCarByLicensePlate((RentalProvider) user, licensePlate);
            model.addAttribute("car", car);
        } catch (IllegalArgumentException | PropertyValueException e) {
            log.error(e.getMessage());
            model.addAttribute(ERROR_ATTRIB_NAME, e.getMessage());
        }

        return "car_display_provider";
    }

    @GetMapping("/cars")
    public String displayAllCars(@AuthenticationPrincipal UserDetails user, Model model) {

        if (!(user instanceof RentalProvider provider)) {
            return REDIRECT_URL;
        }
        model.addAttribute("cars", carService.getAllCar(provider));

        return "car_list";
    }

    @GetMapping("/orders")
    public String displayOrder(@AuthenticationPrincipal UserDetails user, Model model) {
        if (!(user instanceof RentalProvider)) {
            return REDIRECT_URL;
        }

        model.addAttribute("type", PROVIDER_ATTRIB_VALUE);
        return "order-list";
    }

    @GetMapping("/api/all-orders")
    public ResponseEntity<Object> getAllOrders(@AuthenticationPrincipal UserDetails user) {
        if (!(user instanceof RentalProvider)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map<String, Object> response = new HashMap<>();
        List<OrderDisplayDto> orderByProvider = providerService.findAllOrdersInDto((RentalProvider) user);
        response.put("type", PROVIDER_ATTRIB_VALUE);
        response.put("data", orderByProvider);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/current-orders")
    public ResponseEntity<Object> getCurrentOrders(@AuthenticationPrincipal UserDetails user) {
        if (!(user instanceof RentalProvider)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> response = new HashMap<>();
        List<OrderDisplayDto> orderDisplayDtos = providerService.findAllOnGoingOrdersInDto((RentalProvider) user);
        response.put("type", PROVIDER_ATTRIB_VALUE);
        response.put("data", orderDisplayDtos);

        return ResponseEntity.ok(response);
    }
}
