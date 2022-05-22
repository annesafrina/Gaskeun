package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.model.Transmission;
import com.mpp.gaskeun.repository.LocationRepository;
import com.mpp.gaskeun.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/explore")
@Slf4j
public class ExploreController {

    @Autowired
    SearchService searchService;

    @GetMapping
    public String getCars(Model model) {
        List<Car> allCars = searchService.getCars();
        model.addAttribute("allCars", allCars);

        return "explore";
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars(
            Model model,
            @RequestParam(defaultValue = "", name = "budget") long maxPrice,
            @RequestParam(defaultValue = "") Date startDate,
            @RequestParam(defaultValue = "") Date endDate,
            @RequestParam(defaultValue = "") String transmissionType,
            @RequestParam(defaultValue = "", name = "location") String cityName,
            @RequestParam(defaultValue = "", name = "model") String modelName
            ) {
        Transmission transmission = Transmission.valueOf(transmissionType.toUpperCase());
        List<Car> allCars = searchService.getCars(cityName, startDate, endDate, -1, transmission, 0, maxPrice, modelName);

        return ResponseEntity.ok(allCars);
    }
}
