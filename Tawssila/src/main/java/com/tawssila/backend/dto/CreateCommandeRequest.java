package com.tawssila.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommandeRequest(
        @NotNull Long zoneId,
        @NotBlank String adresseDepart,
        @NotBlank String adresseArrivee,
        String description
) {
}
