package com.tawssila.backend.controller;

import com.tawssila.backend.dto.AssignCommandeRequest;
import com.tawssila.backend.dto.CommandeResponse;
import com.tawssila.backend.enums.StatutCommande;
import com.tawssila.backend.service.CommandeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatcher/commandes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DispatcherCommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public List<CommandeResponse> lister(@RequestParam(required = false) StatutCommande statut) {
        if (statut != null) {
            return commandeService.listerParStatut(statut);
        }
        return commandeService.listerToutes();
    }

    @PostMapping("/{id}/assigner")
    public CommandeResponse assigner(@PathVariable Long id,
                                     @Valid @RequestBody AssignCommandeRequest request) {
        return commandeService.assigner(id, request);
    }
}
