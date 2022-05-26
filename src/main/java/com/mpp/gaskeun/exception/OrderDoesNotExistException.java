package com.mpp.gaskeun.exception;

public class OrderDoesNotExistException extends IllegalArgumentException {
    public OrderDoesNotExistException(Long id) {
        super(String.format("Order with id %d not found", id));
    }
}
