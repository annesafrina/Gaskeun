package com.mpp.gaskeun.model;

import javax.persistence.*;

@Entity
@Table(name="Location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "city_name", nullable = false)
    private String cityName;
}
