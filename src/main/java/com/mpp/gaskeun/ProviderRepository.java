package com.mpp.gaskeun;

import com.mpp.gaskeun.model.RentalProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<RentalProvider, Long> {
    Optional<RentalProvider> findByEmail(String email);
}
