package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="review_type",  discriminatorType = DiscriminatorType.STRING)
@Setter @Getter
public abstract class Review {
    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    protected Order order;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "description")
    private String description;

    public abstract void updateRevieweeRating();

    public abstract void updateOrderReviewers();
}
