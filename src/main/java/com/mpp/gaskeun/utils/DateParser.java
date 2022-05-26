package com.mpp.gaskeun.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateParser {
    private DateParser() {
        throw new IllegalStateException("Utility class");
    }
    public static String parse(Date date) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return String.format("%d %s %d", date.getDate(), months[date.getMonth()], date.getYear()+1900);
    }
}
