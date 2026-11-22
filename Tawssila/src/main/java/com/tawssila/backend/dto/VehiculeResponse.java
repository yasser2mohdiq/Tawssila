package com.tawssila.backend.dto;

import com.tawssila.backend.enums.TypeVehicule;

public record VehiculeResponse(
        Long id,
        TypeVehicule type,
        String matricule
) {
}
