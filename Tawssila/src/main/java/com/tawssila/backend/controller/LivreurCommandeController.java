package com.tawssila.backend.controller;

import com.tawssila.backend.dto.CommandeResponse;
import com.tawssila.backend.service.CommandeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreur/commandes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class LivreurCommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public List<CommandeResponse> mesCommandes(Authentication authentication) {
        return commandeService.mesCommandesLivreur(authentication.getName());
    }

    @PostMapping("/{id}/demarrer")
    public CommandeResponse demarrer(Authentication authentication, @PathVariable Long id) {
        return commandeService.demarrer(authentication.getName(), id);
    }

    @PostMapping("/{id}/livrer")
    public CommandeResponse livrer(Authentication authentication, @PathVariable Long id) {
        return commandeService.livrer(authentication.getName(), id);
    }
}
