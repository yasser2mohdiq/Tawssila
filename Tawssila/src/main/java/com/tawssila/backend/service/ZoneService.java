package com.tawssila.backend.service;

import com.tawssila.backend.dto.ZoneRequest;
import com.tawssila.backend.dto.ZoneResponse;
import com.tawssila.backend.entity.Zone;
import com.tawssila.backend.exception.DuplicateResourceException;
import com.tawssila.backend.exception.InvalidCommandeStateException;
import com.tawssila.backend.exception.ResourceNotFoundException;
import com.tawssila.backend.repository.CommandeRepository;
import com.tawssila.backend.repository.LivreurRepository;
import com.tawssila.backend.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;

    @Transactional(readOnly = true)
    public List<ZoneResponse> lister() {
        return zoneRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ZoneResponse get(Long id) {
        return toResponse(findZone(id));
    }

    @Transactional
    public ZoneResponse creer(ZoneRequest request) {
        String nom = request.nom().trim();
        if (zoneRepository.existsByNomIgnoreCase(nom)) {
            throw new DuplicateResourceException("Zone déjà existante: " + nom);
        }
        Zone zone = new Zone();
        zone.setNom(nom);
        zone.setTarifBase(request.tarifBase());
        return toResponse(zoneRepository.save(zone));
    }

    @Transactional
    public ZoneResponse modifier(Long id, ZoneRequest request) {
        Zone zone = findZone(id);
        String nom = request.nom().trim();
        zoneRepository.findByNomIgnoreCase(nom)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Zone déjà existante: " + nom);
                });
        zone.setNom(nom);
        zone.setTarifBase(request.tarifBase());
        return toResponse(zone);
    }

    @Transactional
    public void supprimer(Long id) {
        Zone zone = findZone(id);
        if (commandeRepository.existsByZoneId(id)) {
            throw new InvalidCommandeStateException("Impossible de supprimer: des commandes utilisent cette zone");
        }
        if (livreurRepository.existsByZoneId(id)) {
            throw new InvalidCommandeStateException("Impossible de supprimer: des livreurs sont liés à cette zone");
        }
        zoneRepository.delete(zone);
    }

    private Zone findZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable: " + id));
    }

    private ZoneResponse toResponse(Zone zone) {
        return new ZoneResponse(zone.getId(), zone.getNom(), zone.getTarifBase());
    }
}
