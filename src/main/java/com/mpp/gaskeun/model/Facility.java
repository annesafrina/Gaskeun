package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="facility")
@Getter @Setter
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
}
