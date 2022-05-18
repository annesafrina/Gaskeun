package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CustomerDto extends UserDto {
    private String id_card;
    private String driving_license;
}
