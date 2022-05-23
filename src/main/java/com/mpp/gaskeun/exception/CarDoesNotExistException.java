package com.mpp.gaskeun.exception;

public class CarDoesNotExistException extends IllegalStateException{
    public CarDoesNotExistException(String license) {
        super(String.format("Car with license plate %s not found", license));
    }

    public CarDoesNotExistException(long id) {
        super(String.format("Car with id %d not found", id));
    }
}
