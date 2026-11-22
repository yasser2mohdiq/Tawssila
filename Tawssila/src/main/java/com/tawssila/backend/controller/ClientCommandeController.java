package com.tawssila.backend.controller;

import com.tawssila.backend.dto.CommandeResponse;
import com.tawssila.backend.dto.CreateCommandeRequest;
import com.tawssila.backend.service.CommandeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/commandes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ClientCommandeController {

    private final CommandeService commandeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeResponse creer(Authentication authentication,
                                  @Valid @RequestBody CreateCommandeRequest request) {
        return commandeService.creer(authentication.getName(), request);
    }

    @GetMapping
    public List<CommandeResponse> mesCommandes(Authentication authentication) {
        return commandeService.mesCommandesClient(authentication.getName());
    }

    @GetMapping("/{id}")
    public CommandeResponse detail(Authentication authentication, @PathVariable Long id) {
        return commandeService.getPourClient(authentication.getName(), id);
    }

    @PostMapping("/{id}/annuler")
    public CommandeResponse annuler(Authentication authentication, @PathVariable Long id) {
        return commandeService.annulerParClient(authentication.getName(), id);
    }
}
