package com.mpp.gaskeun.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter @Setter
@Entity
@Table(name="customer")
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="id_card_number", nullable = false)
    private String idCardNumber;

    @Column(name="driving_license_number", nullable = false)
    private String drivingLicenseNumber;

    @Column(name="rating")
    private double rating;

    @Column(name="number_of_reviews")
    private int numberOfReviews = 0;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
