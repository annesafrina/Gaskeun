package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
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

    @GetMapping("/api")
    public ResponseEntity<List<Car>> getCars(
            Model model,
            @RequestParam(defaultValue = "0", name = "budget") long maxPrice,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "") String transmissionType,
            @RequestParam(defaultValue = "", name = "location") String cityName,
            @RequestParam(defaultValue = "", name = "model") String modelName
            ) throws ParseException {
        List<Car> allCars = searchService.getCars(cityName, startDate, endDate, -1, transmissionType, 0, maxPrice, modelName);

        return ResponseEntity.ok(allCars);
    }
}
