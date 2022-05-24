package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CarDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Controller
@RequestMapping("/provider")
@Slf4j
public class ProviderController {

    @Autowired
    private CarService carService;

    @Autowired
    private ProviderService providerService;

    @GetMapping("/info")
    public String getProviderInfo(@AuthenticationPrincipal UserDetails user, Model model) {
        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        model.addAttribute("provider", user);
        model.addAttribute("numCarsOwned", providerService.getNumberOfCarRegistered((RentalProvider) user));
        return "provider_info";
    }

    @GetMapping("/edit")
    public String updateProviderInfo(@AuthenticationPrincipal UserDetails user, Model model) {
        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        UserDto userDto = new UserDto();
        userDto.fillDto((RentalProvider) user);

        model.addAttribute("userDto", userDto);
        return "provider_edit";
    }

    @PostMapping("/edit")
    public String updateProviderInfoPost(@AuthenticationPrincipal UserDetails user,
                                         @ModelAttribute UserDto userDto,
                                         Model model) {

        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        try {
            providerService.update((RentalProvider) user, userDto);
        } catch (IllegalArgumentException e) {
            userDto.fillDto((RentalProvider) user);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userDto", userDto);
            return "provider_edit";
        }

        return "redirect:/provider/info";
    }

    @GetMapping("/register-car")
    public String registerCar(@AuthenticationPrincipal UserDetails user, Model model) {

        if (!(user instanceof RentalProvider))
            return "redirect:/";

        CarDto dto = new CarDto();
        model.addAllAttributes(Map.of(
                "carDto", dto,
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
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/provider/register-car";
    }

    @GetMapping("/update-car/{carId}")
    public String updateCar(
            @PathVariable("carId") long carId,
            @AuthenticationPrincipal UserDetails provider,
            Model model) {

        if (!(provider instanceof RentalProvider))
            return "redirect:/";

        CarDto carDto = new CarDto();

        try {
            Car car = carService.getCarById((RentalProvider) provider, carId);
            carDto.fillDto(car);
            model.addAllAttributes(Map.of(
                    "carDto", carDto,
                    "colors", Color.values(),
                    "transmissions", Transmission.values(),
                    "locations", carService.getAllLocations()
            ));
            log.info("Car found {}", car.getLicensePlate());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
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
            return "redirect:/";

        try {
            log.info("Received car with license {}", carDto.getLicensePlate());
            carDto.setImage(imageFile);
            carService.updateCar((RentalProvider) user, carDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("carDto", carDto);

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
            return "redirect:/";

        try {
            Car car = carService.getCarByLicensePlate((RentalProvider) user, licensePlate);
            model.addAttribute("car", car);
        } catch (IllegalArgumentException | PropertyValueException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }

        return "car_display_provider";
    }

    @GetMapping("/cars")
    public String displayAllCars(@AuthenticationPrincipal UserDetails user, Model model) {
        if (!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        model.addAttribute("cars", carService.getAllCar((RentalProvider) user));

        return "all_cars";
    }
}
