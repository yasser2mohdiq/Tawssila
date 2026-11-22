package com.tawssila.backend.dto;

public record LivreurResponse(
        Long id,
        String nom,
        String email,
        String telephone,
        String cin,
        boolean disponible,
        boolean actif,
        Long zoneId,
        String zoneNom,
        Long vehiculeId,
        String vehiculeMatricule
) {
}
