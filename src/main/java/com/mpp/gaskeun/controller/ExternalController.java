package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.LocationDto;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class ExternalController {

    @Autowired
    private CarService carService;

    @PostMapping("/location")
    public ResponseEntity<?> addLocation(@RequestBody LocationDto locationDto) {
        Location newLocation = new Location();
        newLocation.setCityName(locationDto.getCityName());

        Location generatedLocation;

        try {
            generatedLocation = carService.addLocation(newLocation);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(generatedLocation, HttpStatus.OK);
    }

    @GetMapping("/all-locations")
    public ResponseEntity<?> getAllLocation() {
        return ResponseEntity.ok(
                carService.getAllLocations().stream()
                        .map(Location::getCityName)
                        .toList()
        );
    }
}
