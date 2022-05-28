package com.mpp.gaskeun.utils;

import java.util.Calendar;
import java.util.Date;

public class DateParser {
    private DateParser() {
        throw new IllegalStateException("Utility class");
    }

    public static String parse(Date date) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return String.format("%d %s %d",
                calendar.get(Calendar.DATE),
                months[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR));
    }
}
