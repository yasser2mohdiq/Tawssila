package com.tawssila.backend.controller;

import com.tawssila.backend.dto.ZoneResponse;
import com.tawssila.backend.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/zones")
@RequiredArgsConstructor
public class PublicZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public List<ZoneResponse> lister() {
        return zoneService.lister();
    }
}
