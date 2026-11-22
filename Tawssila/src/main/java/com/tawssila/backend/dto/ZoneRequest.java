package com.tawssila.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ZoneRequest(
        @NotBlank String nom,
        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être > 0")
        BigDecimal tarifBase
) {
}
