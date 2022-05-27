package com.mpp.gaskeun.utils;

import com.mpp.gaskeun.dto.OrderDisplayDto;
import com.mpp.gaskeun.dto.OrderDto;
import com.mpp.gaskeun.model.DateRange;
import com.mpp.gaskeun.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderUtils {

    private OrderUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<DateRange> findUnavailableDates(List<Order> orderList) {
        List<DateRange> unavailableDates = new ArrayList<>();

        for (Order order: orderList) {
            Date startDate = order.getStartDate();
            Date endDate = order.getEndDate();
            DateRange dateRange = new DateRange(startDate, endDate);
            unavailableDates.add(dateRange);
        }

        return unavailableDates;
    }

    public static OrderDisplayDto lightDisplayOrder(Order order) {
        String startDate = DateParser.parse(order.getStartDate());
        String endDate = DateParser.parse(order.getEndDate());
        OrderDisplayDto dto = new OrderDisplayDto(order.getId(), order.getCar().getModel(), order.getOrderStatus(), startDate, endDate, order.isCustomerIsReviewed(), order.isCarIsReviewed(), order.isProviderIsReviewed());
        return dto;
    }
}
