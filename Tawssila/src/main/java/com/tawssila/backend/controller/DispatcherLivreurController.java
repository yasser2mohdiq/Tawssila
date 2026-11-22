package com.tawssila.backend.controller;

import com.tawssila.backend.dto.LivreurResponse;
import com.tawssila.backend.service.LivreurService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dispatcher/livreurs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DispatcherLivreurController {

    private final LivreurService livreurService;

    @GetMapping
    public List<LivreurResponse> lister(@RequestParam(required = false, defaultValue = "false") boolean disponibles) {
        if (disponibles) {
            return livreurService.listerDisponibles();
        }
        return livreurService.listerTous();
    }
}
