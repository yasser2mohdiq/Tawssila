package com.tawssila.backend.service;

import com.tawssila.backend.dto.LivreurResponse;
import com.tawssila.backend.entity.Livreur;
import com.tawssila.backend.repository.LivreurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivreurService {

    private final LivreurRepository livreurRepository;

    @Transactional(readOnly = true)
    public List<LivreurResponse> listerTous() {
        return livreurRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LivreurResponse> listerDisponibles() {
        return livreurRepository.findByDisponibleTrueAndActifTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    private LivreurResponse toResponse(Livreur livreur) {
        return new LivreurResponse(
                livreur.getId(),
                livreur.getNom(),
                livreur.getEmail(),
                livreur.getTelephone(),
                livreur.getCin(),
                livreur.isDisponible(),
                livreur.isActif(),
                livreur.getZone() != null ? livreur.getZone().getId() : null,
                livreur.getZone() != null ? livreur.getZone().getNom() : null,
                livreur.getVehicule() != null ? livreur.getVehicule().getId() : null,
                livreur.getVehicule() != null ? livreur.getVehicule().getMatricule() : null
        );
    }
}
