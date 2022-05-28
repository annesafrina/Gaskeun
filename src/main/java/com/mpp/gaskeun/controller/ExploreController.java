package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.model.Car;
import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("")
    public String getCars(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isNotLoggedIn = authentication == null || authentication instanceof AnonymousAuthenticationToken;
        model.addAttribute("isNotLoggedIn", isNotLoggedIn);

        if (!isNotLoggedIn) {
            UserDetails user = (UserDetails) authentication.getPrincipal();

            model.addAttribute("type", (user instanceof Customer) ? "customer" : "provider");
        }

        return "explore";
    }

    @GetMapping("/api")
    public ResponseEntity<List<Car>> getCars(
            @RequestParam(defaultValue = "0", name = "budget") long maxPrice,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "") String transmissionType,
            @RequestParam(defaultValue = "", name = "location") String cityName,
            @RequestParam(defaultValue = "", name = "model") String modelName
    ) throws ParseException {
        List<Car> allCars = searchService.getCars(cityName, startDate, endDate, transmissionType, 0, maxPrice, modelName);

        return ResponseEntity.ok(allCars);
    }

    @GetMapping("/api/model-names")
    public ResponseEntity<List<String>> getCarNames() {
        return ResponseEntity.ok(searchService.getCarNames());
    }
}
