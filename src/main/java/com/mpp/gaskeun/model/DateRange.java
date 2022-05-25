package com.mpp.gaskeun.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class DateRange {
    private Date startingDate;
    private Date endDate;

    public boolean dateInRange(Date date) {
        return startingDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0;
    }
}
