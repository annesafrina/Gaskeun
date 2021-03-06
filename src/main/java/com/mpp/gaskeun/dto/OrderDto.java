package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto {
    private String startDate;
    private String endDate;
    private String pickUpLocation;
    private String dropOffLocation;
    private String carId;
}
