package com.tawssila.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livreurs")
@Getter @Setter @NoArgsConstructor
public class Livreur extends Utilisateur {
    @Column(unique = true)
    private String cin;

    private boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;
}
