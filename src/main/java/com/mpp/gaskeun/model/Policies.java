package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "policies")
@Getter @Setter
public class Policies {
    @EmbeddedId
    private PoliciesId id;

    @MapsId("car_id")
    @JoinColumns({
            @JoinColumn(name="car_id", referencedColumnName = "id")
    })
    @ManyToOne
    private Car car;

    @Column(name = "policies")
    private String policies;
}
