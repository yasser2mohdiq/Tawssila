package com.tawssila.backend.dto;

public record UserResponse(
        Long id,
        String email,
        String nom,
        String telephone,
        String role,
        boolean actif
) {
}
