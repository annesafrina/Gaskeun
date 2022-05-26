package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Location")
@Setter
@Getter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "city_name", nullable = false)
    private String cityName;
}
