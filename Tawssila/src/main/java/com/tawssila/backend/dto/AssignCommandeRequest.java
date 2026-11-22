package com.tawssila.backend.dto;

import jakarta.validation.constraints.NotNull;

public record AssignCommandeRequest(
        @NotNull Long livreurId
) {
}
