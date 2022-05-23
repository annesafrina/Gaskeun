package com.mpp.gaskeun.dto;

import com.mpp.gaskeun.model.RentalProvider;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserDto {
    private long id;
    private String email;
    private String phone_number;
    private String name;
    private String password;
    private String passwordConfirmation;
    private String oldPassword;

    public void fillDto(RentalProvider provider) {
        id = provider.getId();
        email = provider.getEmail();
        phone_number = provider.getPhoneNumber();
        name = provider.getName();
    }

    public boolean containsPassword(){
        return password != null && !password.isBlank();
    }

    public boolean isValid() {
        return !name.isBlank() && !phone_number.isBlank();
    }
}
