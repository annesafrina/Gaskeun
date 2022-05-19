package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class CarDto {
    String licensePlate;
    int capacity;
    String transmission;
    long priceRate;
    String color;
    String model;
    String availableStart;
    String availableEnd;
    String base64image;
}
