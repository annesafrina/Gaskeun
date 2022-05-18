package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter @Setter
public class PoliciesId implements Serializable {
    @Column(name = "car_id")
    private long employee_id;

    @Column(name = "payroll_id")
    private long payroll_id;
}