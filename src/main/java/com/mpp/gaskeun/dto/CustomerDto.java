package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CustomerDto extends UserDto {
    private String idCard;
    private String drivingLicense;

    @Override
    public boolean isComplete() {
        return super.isComplete() &&
                !(idCard == null || idCard.isBlank()) &&
                !(drivingLicense == null || drivingLicense.isBlank());
    }
}
