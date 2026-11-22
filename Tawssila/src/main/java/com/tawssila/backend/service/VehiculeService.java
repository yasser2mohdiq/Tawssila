package com.tawssila.backend.service;

import com.tawssila.backend.dto.VehiculeRequest;
import com.tawssila.backend.dto.VehiculeResponse;
import com.tawssila.backend.entity.Vehicule;
import com.tawssila.backend.exception.DuplicateResourceException;
import com.tawssila.backend.exception.InvalidCommandeStateException;
import com.tawssila.backend.exception.ResourceNotFoundException;
import com.tawssila.backend.repository.LivreurRepository;
import com.tawssila.backend.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final LivreurRepository livreurRepository;

    @Transactional(readOnly = true)
    public List<VehiculeResponse> lister() {
        return vehiculeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VehiculeResponse get(Long id) {
        return toResponse(findVehicule(id));
    }

    @Transactional
    public VehiculeResponse creer(VehiculeRequest request) {
        String matricule = request.matricule().trim().toUpperCase();
        if (vehiculeRepository.existsByMatriculeIgnoreCase(matricule)) {
            throw new DuplicateResourceException("Matricule déjà utilisé: " + matricule);
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setType(request.type());
        vehicule.setMatricule(matricule);
        return toResponse(vehiculeRepository.save(vehicule));
    }

    @Transactional
    public VehiculeResponse modifier(Long id, VehiculeRequest request) {
        Vehicule vehicule = findVehicule(id);
        String matricule = request.matricule().trim().toUpperCase();
        vehiculeRepository.findByMatriculeIgnoreCase(matricule)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Matricule déjà utilisé: " + matricule);
                });
        vehicule.setType(request.type());
        vehicule.setMatricule(matricule);
        return toResponse(vehicule);
    }

    @Transactional
    public void supprimer(Long id) {
        Vehicule vehicule = findVehicule(id);
        if (livreurRepository.existsByVehiculeId(id)) {
            throw new InvalidCommandeStateException("Impossible de supprimer: un livreur utilise ce véhicule");
        }
        vehiculeRepository.delete(vehicule);
    }

    private Vehicule findVehicule(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable: " + id));
    }

    private VehiculeResponse toResponse(Vehicule vehicule) {
        return new VehiculeResponse(vehicule.getId(), vehicule.getType(), vehicule.getMatricule());
    }
}
