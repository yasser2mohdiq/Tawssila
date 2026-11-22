package com.tawssila.backend.controller;

import com.tawssila.backend.dto.VehiculeRequest;
import com.tawssila.backend.dto.VehiculeResponse;
import com.tawssila.backend.service.VehiculeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vehicules")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminVehiculeController {

    private final VehiculeService vehiculeService;

    @GetMapping
    public List<VehiculeResponse> lister() {
        return vehiculeService.lister();
    }

    @GetMapping("/{id}")
    public VehiculeResponse get(@PathVariable Long id) {
        return vehiculeService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeResponse creer(@Valid @RequestBody VehiculeRequest request) {
        return vehiculeService.creer(request);
    }

    @PutMapping("/{id}")
    public VehiculeResponse modifier(@PathVariable Long id, @Valid @RequestBody VehiculeRequest request) {
        return vehiculeService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        vehiculeService.supprimer(id);
    }
}
