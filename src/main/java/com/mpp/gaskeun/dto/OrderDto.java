package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class OrderDto {
    private Date startDate;
    private Date endDate;
    private String pickUpLocation;
    private String dropOffLocation;
    private String carId;
}
