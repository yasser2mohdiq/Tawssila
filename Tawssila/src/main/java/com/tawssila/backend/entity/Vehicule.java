package com.tawssila.backend.entity;

import com.tawssila.backend.enums.TypeVehicule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicules")
@Getter @Setter @NoArgsConstructor
public class Vehicule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVehicule type;

    @Column(nullable = false, unique = true)
    private String matricule;
}
