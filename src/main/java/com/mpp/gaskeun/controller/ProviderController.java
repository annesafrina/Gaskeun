package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.model.*;
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
    private ProviderService providerService;

    @GetMapping("/register-car")
    public String registerCar(@AuthenticationPrincipal UserDetails user, Model model) {

        if(!(user instanceof RentalProvider))
            return "redirect:/";

        CarDto dto = new CarDto();
        model.addAllAttributes(Map.of(
                "carDto", dto,
                "colors", Color.values(),
                "transmissions", Transmission.values(),
                "locations", providerService.getAllLocations()
        ));

        return "car_registration";
    }

    @PostMapping("/register-car")
    public String registerCarPost(@AuthenticationPrincipal UserDetails user,
                                  @ModelAttribute CarDto carDto,
                                  @RequestParam("car-image")MultipartFile imageFile,
                                  Model model) throws IOException, ParseException {

        if(!(user instanceof RentalProvider)) {
            return "redirect:/";
        }

        try {
            carDto.setImage(imageFile);
            providerService.addCar((RentalProvider) user, carDto);
        } catch (IllegalStateException e) {
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

        if(!(provider instanceof RentalProvider))
            return "redirect:/";

        CarDto carDto = new CarDto();

        try {
            Car car = providerService.getCarById((RentalProvider) provider, carId);
            carDto.fillDto(car);
            model.addAllAttributes(Map.of(
                    "carDto", carDto,
                    "colors", Color.values(),
                    "transmissions", Transmission.values(),
                    "locations", providerService.getAllLocations()
            ));
            log.info("Car found {}", car.getLicensePlate());
        } catch (IllegalStateException e) {
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

        if(!(user instanceof RentalProvider))
            return "redirect:/";

        log.info("UPC {}", carDto.getBase64image());

        try {
            log.info("Received car with license {}", carDto.getLicensePlate());
            carDto.setImage(imageFile);
            providerService.updateCar((RentalProvider) user, carDto);
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
            @AuthenticationPrincipal UserDetails provider,
            Model model) {

        log.info(licensePlate);
        if(!(provider instanceof RentalProvider))
            return "redirect:/";

        try {
            Car car = providerService.getCarByLicensePlate((RentalProvider) provider, licensePlate);
            model.addAttribute("car", car);
        } catch (IllegalStateException | PropertyValueException e){
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }

        return "car_display_provider";
    }
}
