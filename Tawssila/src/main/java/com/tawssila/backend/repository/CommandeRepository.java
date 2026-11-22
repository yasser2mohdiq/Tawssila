package com.tawssila.backend.repository;

import com.tawssila.backend.entity.Commande;
import com.tawssila.backend.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientIdOrderByCreeLeDesc(Long clientId);

    List<Commande> findByLivreurIdOrderByCreeLeDesc(Long livreurId);

    List<Commande> findByStatutOrderByCreeLeAsc(StatutCommande statut);

    List<Commande> findAllByOrderByCreeLeDesc();

    boolean existsByZoneId(Long zoneId);
}
