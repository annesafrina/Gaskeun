package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.dto.LocationDto;
import com.mpp.gaskeun.model.Location;
import com.mpp.gaskeun.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class ExternalController {

    @Autowired
    private CarService providerService;

    @PostMapping("/location")
    public ResponseEntity<?> addLocation(@RequestBody LocationDto locationDto) {
        Location newLocation = new Location();
        newLocation.setCityName(locationDto.getCityName());

        Location generatedLocation;

        try {
            generatedLocation = providerService.addLocation(newLocation);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(generatedLocation, HttpStatus.OK);
    }
}
