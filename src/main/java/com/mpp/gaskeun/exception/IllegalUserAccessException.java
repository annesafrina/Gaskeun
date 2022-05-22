package com.mpp.gaskeun.exception;

public class IllegalUserAccessException extends IllegalStateException {
    public IllegalUserAccessException(long id, String email) {
        super(String.format("User %s is not authorized to access order with id %s", email, id));
    }
}
