package com.mpp.gaskeun.dto;

import com.mpp.gaskeun.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class OrderDisplayDto {
    private long id;
    private String carName;
    private OrderStatus status;
    private String startDate;
    private String endDate;
}
