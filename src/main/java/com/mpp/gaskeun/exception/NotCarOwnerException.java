package com.mpp.gaskeun.exception;

public class NotCarOwnerException extends IllegalArgumentException {
    public NotCarOwnerException(String providerEmail, String carLicense) {
        super(String.format("Car with license %s does not belong to %s", carLicense, providerEmail));
    }
}
