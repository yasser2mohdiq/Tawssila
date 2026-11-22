package com.tawssila.backend.dto;

import com.tawssila.backend.enums.TypeVehicule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehiculeRequest(
        @NotNull TypeVehicule type,
        @NotBlank String matricule
) {
}
