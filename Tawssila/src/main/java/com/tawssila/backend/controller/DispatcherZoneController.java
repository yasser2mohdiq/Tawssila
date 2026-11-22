package com.tawssila.backend.controller;

import com.tawssila.backend.dto.ZoneRequest;
import com.tawssila.backend.dto.ZoneResponse;
import com.tawssila.backend.service.ZoneService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatcher/zones")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DispatcherZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public List<ZoneResponse> lister() {
        return zoneService.lister();
    }

    @GetMapping("/{id}")
    public ZoneResponse get(@PathVariable Long id) {
        return zoneService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZoneResponse creer(@Valid @RequestBody ZoneRequest request) {
        return zoneService.creer(request);
    }

    @PutMapping("/{id}")
    public ZoneResponse modifier(@PathVariable Long id, @Valid @RequestBody ZoneRequest request) {
        return zoneService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        zoneService.supprimer(id);
    }
}
