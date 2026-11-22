package com.tawssila.backend.dto;

import java.math.BigDecimal;

public record ZoneResponse(
        Long id,
        String nom,
        BigDecimal tarifBase
) {
}
