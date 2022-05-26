package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.RentalProvider;

public interface ProviderService {
    long getNumberOfCarRegistered(RentalProvider provider);
    RentalProvider update(RentalProvider provider, UserDto userDto);
}
