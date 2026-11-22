package com.tawssila.backend.dto;

public record AuthResponse(
        String token,
        String email,
        String nom,
        String role
) {
}
