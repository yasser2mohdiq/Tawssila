package com.tawssila.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor
public class Client extends Utilisateur {
    private String adresse;
}
