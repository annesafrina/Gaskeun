package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.CarDto;
import com.mpp.gaskeun.model.*;
import com.mpp.gaskeun.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping("/provider")
@Slf4j
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/register-car")
    public String registerCar(Model model) {
        CarDto dto = new CarDto();

        model.addAttribute("carDto", dto);
        model.addAttribute("colors", Color.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("locations", providerService.getAllLocations());
        return "car_registration";
    }

    @PostMapping("/register-car")
    public String registerCarPost(@AuthenticationPrincipal RentalProvider provider,
                                  @ModelAttribute CarDto carDto,
                                  @RequestParam("car-image")MultipartFile file,
                                  Model model) throws IOException, ParseException {

        log.info("Registering car");
        byte[] image = Base64.encodeBase64(file.getBytes());
        String base64Image = new String(image);

        carDto.setBase64image(base64Image);
        try {
            providerService.addCar(provider, carDto);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/provider/register-car";
    }

    @GetMapping("/update-car")
    public String updateCar(@RequestParam("id") Long carId) {

        return null;
    }

    @GetMapping("/car-detail/{licensePlate}")
    public String displayCar(
            @PathVariable("licensePlate") String licensePlate,
            @AuthenticationPrincipal UserDetails provider,
            Model model) {

        if(!(provider instanceof RentalProvider))
            return "redirect:/";

        try {
            Car car = providerService.getCarByLicensePlate((RentalProvider) provider, licensePlate);
            log.info("Get: {}, Expected: {}", car.getLicensePlate(), licensePlate);
            model.addAttribute("car", car);

        } catch (IllegalStateException e){
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }

        return "car_display_provider";
    }
}
