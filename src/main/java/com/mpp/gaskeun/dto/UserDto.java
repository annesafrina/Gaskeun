package com.mpp.gaskeun.dto;

import com.mpp.gaskeun.model.RentalProvider;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private long id;
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
    private String passwordConfirmation;
    private String oldPassword;

    public void fillDto(RentalProvider provider) {
        id = provider.getId();
        email = provider.getEmail();
        phoneNumber = provider.getPhoneNumber();
        name = provider.getName();
    }

    public boolean containsPassword() {
        return password != null && !password.isBlank();
    }

    public boolean containNameAndPhoneNumber() {
        return !(name == null || name.isBlank()) &&
                !(phoneNumber == null || phoneNumber.isBlank());
    }

    public boolean isComplete() {
        return !(email == null || email.isBlank()) &&
                !(phoneNumber == null || phoneNumber.isBlank()) &&
                !(name == null || name.isBlank()) &&
                !(password == null || password.length() < 8);
    }

    public boolean passwordMatches() {
        return password != null && password.equals(passwordConfirmation);
    }
}
