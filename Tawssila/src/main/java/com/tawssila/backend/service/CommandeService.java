package com.tawssila.backend.service;

import com.tawssila.backend.dto.AssignCommandeRequest;
import com.tawssila.backend.dto.CommandeResponse;
import com.tawssila.backend.dto.CreateCommandeRequest;
import com.tawssila.backend.entity.Client;
import com.tawssila.backend.entity.Commande;
import com.tawssila.backend.entity.Livreur;
import com.tawssila.backend.entity.Zone;
import com.tawssila.backend.enums.StatutCommande;
import com.tawssila.backend.exception.InvalidCommandeStateException;
import com.tawssila.backend.exception.ResourceNotFoundException;
import com.tawssila.backend.repository.ClientRepository;
import com.tawssila.backend.repository.CommandeRepository;
import com.tawssila.backend.repository.LivreurRepository;
import com.tawssila.backend.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;

    @Transactional
    public CommandeResponse creer(String clientEmail, CreateCommandeRequest request) {
        Client client = findClientByEmail(clientEmail);
        Zone zone = zoneRepository.findById(request.zoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable: " + request.zoneId()));

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setZone(zone);
        commande.setAdresseDepart(request.adresseDepart().trim());
        commande.setAdresseArrivee(request.adresseArrivee().trim());
        commande.setDescription(blankToNull(request.description()));
        commande.setTarif(zone.getTarifBase());
        commande.setStatut(StatutCommande.CREEE);

        return toResponse(commandeRepository.save(commande));
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> mesCommandesClient(String clientEmail) {
        Client client = findClientByEmail(clientEmail);
        return commandeRepository.findByClientIdOrderByCreeLeDesc(client.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CommandeResponse getPourClient(String clientEmail, Long commandeId) {
        Commande commande = findCommande(commandeId);
        ensureClientOwner(commande, clientEmail);
        return toResponse(commande);
    }

    @Transactional
    public CommandeResponse annulerParClient(String clientEmail, Long commandeId) {
        Commande commande = findCommande(commandeId);
        ensureClientOwner(commande, clientEmail);

        if (commande.getStatut() != StatutCommande.CREEE && commande.getStatut() != StatutCommande.ASSIGNEE) {
            throw new InvalidCommandeStateException(
                    "Impossible d'annuler une commande au statut " + commande.getStatut());
        }

        if (commande.getLivreur() != null) {
            commande.getLivreur().setDisponible(true);
        }

        commande.setStatut(StatutCommande.ANNULEE);
        commande.setAnnuleeLe(Instant.now());
        return toResponse(commande);
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> listerToutes() {
        return commandeRepository.findAllByOrderByCreeLeDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> listerParStatut(StatutCommande statut) {
        return commandeRepository.findByStatutOrderByCreeLeAsc(statut).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CommandeResponse assigner(Long commandeId, AssignCommandeRequest request) {
        Commande commande = findCommande(commandeId);
        if (commande.getStatut() != StatutCommande.CREEE) {
            throw new InvalidCommandeStateException(
                    "Seule une commande CREEE peut être assignée (actuel: " + commande.getStatut() + ")");
        }

        Livreur livreur = livreurRepository.findById(request.livreurId())
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable: " + request.livreurId()));

        if (!livreur.isActif()) {
            throw new InvalidCommandeStateException("Le livreur est inactif");
        }
        if (!livreur.isDisponible()) {
            throw new InvalidCommandeStateException("Le livreur n'est pas disponible");
        }

        livreur.setDisponible(false);
        commande.setLivreur(livreur);
        commande.setStatut(StatutCommande.ASSIGNEE);
        commande.setAssigneeLe(Instant.now());
        return toResponse(commande);
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> mesCommandesLivreur(String livreurEmail) {
        Livreur livreur = findLivreurByEmail(livreurEmail);
        return commandeRepository.findByLivreurIdOrderByCreeLeDesc(livreur.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CommandeResponse demarrer(String livreurEmail, Long commandeId) {
        Commande commande = findCommandePourLivreur(livreurEmail, commandeId);
        if (commande.getStatut() != StatutCommande.ASSIGNEE) {
            throw new InvalidCommandeStateException(
                    "La livraison ne peut démarrer que depuis ASSIGNEE (actuel: " + commande.getStatut() + ")");
        }
        commande.setStatut(StatutCommande.EN_COURS);
        commande.setDemarreeLe(Instant.now());
        return toResponse(commande);
    }

    @Transactional
    public CommandeResponse livrer(String livreurEmail, Long commandeId) {
        Commande commande = findCommandePourLivreur(livreurEmail, commandeId);
        if (commande.getStatut() != StatutCommande.EN_COURS) {
            throw new InvalidCommandeStateException(
                    "La commande ne peut être livrée que depuis EN_COURS (actuel: " + commande.getStatut() + ")");
        }
        commande.setStatut(StatutCommande.LIVREE);
        commande.setLivreeLe(Instant.now());
        commande.getLivreur().setDisponible(true);
        return toResponse(commande);
    }

    private Commande findCommandePourLivreur(String livreurEmail, Long commandeId) {
        Commande commande = findCommande(commandeId);
        Livreur livreur = findLivreurByEmail(livreurEmail);
        if (commande.getLivreur() == null || !commande.getLivreur().getId().equals(livreur.getId())) {
            throw new ResourceNotFoundException("Commande introuvable pour ce livreur");
        }
        return commande;
    }

    private void ensureClientOwner(Commande commande, String clientEmail) {
        if (!commande.getClient().getEmail().equalsIgnoreCase(clientEmail)) {
            throw new ResourceNotFoundException("Commande introuvable");
        }
    }

    private Commande findCommande(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable: " + id));
    }

    private Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
    }

    private Livreur findLivreurByEmail(String email) {
        return livreurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable"));
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private CommandeResponse toResponse(Commande commande) {
        return new CommandeResponse(
                commande.getId(),
                commande.getClient().getId(),
                commande.getClient().getNom(),
                commande.getLivreur() != null ? commande.getLivreur().getId() : null,
                commande.getLivreur() != null ? commande.getLivreur().getNom() : null,
                commande.getZone().getId(),
                commande.getZone().getNom(),
                commande.getAdresseDepart(),
                commande.getAdresseArrivee(),
                commande.getDescription(),
                commande.getStatut(),
                commande.getTarif(),
                commande.getCreeLe(),
                commande.getAssigneeLe(),
                commande.getDemarreeLe(),
                commande.getLivreeLe(),
                commande.getAnnuleeLe()
        );
    }
}
