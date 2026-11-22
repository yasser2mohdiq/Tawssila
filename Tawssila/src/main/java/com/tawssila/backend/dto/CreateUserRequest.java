package com.tawssila.backend.dto;

import com.tawssila.backend.enums.RoleNom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String nom,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String motDePasse,
        String telephone,
        @NotNull RoleNom role
) {
}
