package com.mpp.gaskeun.exception;

public class OrderNotReviewableException extends IllegalArgumentException{
    public OrderNotReviewableException(Long id) { super(String.format("Order with id %d is not reviewable", id)); }
}
