package com.tawssila.backend.repository;

import com.tawssila.backend.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Optional<Livreur> findByEmail(String email);

    List<Livreur> findByDisponibleTrueAndActifTrue();

    boolean existsByZoneId(Long zoneId);

    boolean existsByVehiculeId(Long vehiculeId);
}
