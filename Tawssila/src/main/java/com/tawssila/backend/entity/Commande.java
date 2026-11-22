package com.tawssila.backend.entity;

import com.tawssila.backend.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private String adresseDepart;

    @Column(nullable = false)
    private String adresseArrivee;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCommande statut = StatutCommande.CREEE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarif;

    @Column(nullable = false, updatable = false)
    private Instant creeLe;

    private Instant assigneeLe;
    private Instant demarreeLe;
    private Instant livreeLe;
    private Instant annuleeLe;

    @PrePersist
    void onCreate() {
        if (creeLe == null) {
            creeLe = Instant.now();
        }
        if (statut == null) {
            statut = StatutCommande.CREEE;
        }
    }
}
