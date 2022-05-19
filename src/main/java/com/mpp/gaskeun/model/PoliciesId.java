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
    private long car_id;

    @Column(name = "policy_id")
    private long policy_id;
}