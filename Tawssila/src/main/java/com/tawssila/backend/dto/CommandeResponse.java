package com.tawssila.backend.dto;

import com.tawssila.backend.enums.StatutCommande;

import java.math.BigDecimal;
import java.time.Instant;

public record CommandeResponse(
        Long id,
        Long clientId,
        String clientNom,
        Long livreurId,
        String livreurNom,
        Long zoneId,
        String zoneNom,
        String adresseDepart,
        String adresseArrivee,
        String description,
        StatutCommande statut,
        BigDecimal tarif,
        Instant creeLe,
        Instant assigneeLe,
        Instant demarreeLe,
        Instant livreeLe,
        Instant annuleeLe
) {
}
