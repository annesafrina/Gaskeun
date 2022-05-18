package com.mpp.gaskeun.repository;

import com.mpp.gaskeun.model.RentalProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<RentalProvider, Long> {
    Optional<RentalProvider> findByEmail(String email);
}
