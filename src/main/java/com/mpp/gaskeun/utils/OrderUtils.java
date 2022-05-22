package com.mpp.gaskeun.utils;

import com.mpp.gaskeun.model.DateRange;
import com.mpp.gaskeun.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderUtils {

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
}
