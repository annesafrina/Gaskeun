package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CustomerDto extends UserDto {
    private String id_card;
    private String driving_license;

    @Override
    public boolean isComplete() {
        return super.isComplete() &&
                !(id_card == null || id_card.isBlank()) &&
                !(driving_license == null || driving_license.isBlank());
    }
}
