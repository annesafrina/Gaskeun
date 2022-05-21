package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    UserDetails register(UserDto userDto);
}
