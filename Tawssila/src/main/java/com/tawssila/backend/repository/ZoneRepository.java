package com.tawssila.backend.repository;

import com.tawssila.backend.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCase(String nom);
}
