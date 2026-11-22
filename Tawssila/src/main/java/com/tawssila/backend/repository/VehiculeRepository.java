package com.tawssila.backend.repository;

import com.tawssila.backend.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByMatriculeIgnoreCase(String matricule);

    boolean existsByMatriculeIgnoreCase(String matricule);
}
