package com.mpp.gaskeun.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserDto {
    private String email;
    private String phone_number;
    private String name;
    private String password;
    private String passwordConfirmation;
}
