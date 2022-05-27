package com.mpp.gaskeun.service;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.UserDto;
import com.mpp.gaskeun.model.Order;
import com.mpp.gaskeun.model.RentalProvider;

import java.util.List;

public interface ProviderService {
    long getNumberOfCarRegistered(RentalProvider provider);

    List<Order> findAllOrders(RentalProvider provider);

    List<Order> findAllOnGoingOrders(RentalProvider provider);

    List<OrderDisplayDto> findAllOrdersInDto(RentalProvider provider);

    List<OrderDisplayDto> findAllOnGoingOrdersInDto(RentalProvider provider);

    RentalProvider update(RentalProvider provider, UserDto userDto);
}
