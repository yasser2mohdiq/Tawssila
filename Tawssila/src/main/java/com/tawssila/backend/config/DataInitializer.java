package com.tawssila.backend.config;

import com.tawssila.backend.entity.*;
import com.tawssila.backend.enums.RoleNom;
import com.tawssila.backend.enums.StatutCommande;
import com.tawssila.backend.enums.TypeVehicule;
import com.tawssila.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private static final String DEMO_PASSWORD = "Demo123!";

    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ZoneRepository zoneRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final CommandeRepository commandeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            seedRoles();
            seedAdmin();

            Zone centre = seedZone("Centre-ville", "25.00");
            Zone peripherie = seedZone("Périphérie", "40.00");
            Zone banlieue = seedZone("Banlieue", "55.00");

            Vehicule moto1 = seedVehicule(TypeVehicule.MOTO, "12345-A-1");
            Vehicule moto2 = seedVehicule(TypeVehicule.MOTO, "67890-B-2");
            Vehicule voiture = seedVehicule(TypeVehicule.VOITURE, "11223-C-3");
            seedVehicule(TypeVehicule.VELO, "VELO-001");
            seedVehicule(TypeVehicule.CAMIONNETTE, "99887-D-4");

            seedDispatcher("Karim Dispatcher", "dispatcher@tawssila.local", "0611111111");

            Client client1 = seedClient("Yassine Client", "client@tawssila.local", "0622222222", "12 Rue Hassan II, Casablanca");
            Client client2 = seedClient("Sara Benali", "sara@tawssila.local", "0633333333", "45 Bd Zerktouni, Casablanca");
            Client client3 = seedClient("Omar Tazi", "omar@tawssila.local", "0644444444", "8 Rue Ibn Batouta, Rabat");

            Livreur livreur1 = seedLivreur("Hassan Livreur", "livreur@tawssila.local", "0655555555", "AB123456", centre, moto1, true);
            Livreur livreur2 = seedLivreur("Fatima Coureuse", "fatima@tawssila.local", "0666666666", "CD789012", peripherie, moto2, true);
            Livreur livreur3 = seedLivreur("Mehdi Route", "mehdi@tawssila.local", "0677777777", "EF345678", banlieue, voiture, false);

            if (commandeRepository.count() == 0) {
                seedCommande(client1, centre, "12 Rue Hassan II", "Marché Central", "Colis documents", StatutCommande.CREEE, null, null);
                seedCommande(client2, peripherie, "45 Bd Zerktouni", "Aéroport Mohamed V", "Bagage fragile", StatutCommande.ASSIGNEE, livreur1, Instant.now().minus(1, ChronoUnit.HOURS));
                seedCommande(client3, banlieue, "8 Rue Ibn Batouta", "Université Mohammed V", "Livres", StatutCommande.EN_COURS, livreur3, Instant.now().minus(3, ChronoUnit.HOURS));
                seedCommande(client1, centre, "Café Maure", "Gare Casa-Port", "Clés", StatutCommande.LIVREE, livreur2, Instant.now().minus(1, ChronoUnit.DAYS));
                seedCommande(client2, centre, "Mall Morocco", "Habous", "Retour produit", StatutCommande.ANNULEE, null, null);
            }

            // livreur3 est déjà sur une commande EN_COURS → indisponible
            if (livreur3.isDisponible()) {
                livreur3.setDisponible(false);
                livreurRepository.save(livreur3);
            }
            // livreur1 a une commande ASSIGNEE → indisponible
            if (livreur1.isDisponible()) {
                livreur1.setDisponible(false);
                livreurRepository.save(livreur1);
            }
        };
    }

    private void seedRoles() {
        for (RoleNom nom : RoleNom.values()) {
            if (roleRepository.findByNom(nom).isEmpty()) {
                roleRepository.save(new Role(null, nom));
            }
        }
    }

    private void seedAdmin() {
        if (!utilisateurRepository.existsByEmail(adminEmail)) {
            Role adminRole = role(RoleNom.ADMIN);
            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setEmail(adminEmail);
            admin.setMotDePasse(passwordEncoder.encode(adminPassword));
            admin.setTelephone("0600000000");
            admin.setRole(adminRole);
            admin.setActif(true);
            utilisateurRepository.save(admin);
        }
    }

    private Zone seedZone(String nom, String tarif) {
        return zoneRepository.findByNomIgnoreCase(nom).orElseGet(() -> {
            Zone zone = new Zone();
            zone.setNom(nom);
            zone.setTarifBase(new BigDecimal(tarif));
            return zoneRepository.save(zone);
        });
    }

    private Vehicule seedVehicule(TypeVehicule type, String matricule) {
        return vehiculeRepository.findByMatriculeIgnoreCase(matricule).orElseGet(() -> {
            Vehicule vehicule = new Vehicule();
            vehicule.setType(type);
            vehicule.setMatricule(matricule);
            return vehiculeRepository.save(vehicule);
        });
    }

    private void seedDispatcher(String nom, String email, String telephone) {
        if (utilisateurRepository.existsByEmail(email)) {
            return;
        }
        Utilisateur dispatcher = new Utilisateur();
        dispatcher.setNom(nom);
        dispatcher.setEmail(email);
        dispatcher.setMotDePasse(passwordEncoder.encode(DEMO_PASSWORD));
        dispatcher.setTelephone(telephone);
        dispatcher.setRole(role(RoleNom.DISPATCHER));
        dispatcher.setActif(true);
        utilisateurRepository.save(dispatcher);
    }

    private Client seedClient(String nom, String email, String telephone, String adresse) {
        return clientRepository.findByEmail(email).orElseGet(() -> {
            Client client = new Client();
            client.setNom(nom);
            client.setEmail(email);
            client.setMotDePasse(passwordEncoder.encode(DEMO_PASSWORD));
            client.setTelephone(telephone);
            client.setAdresse(adresse);
            client.setRole(role(RoleNom.CLIENT));
            client.setActif(true);
            return clientRepository.save(client);
        });
    }

    private Livreur seedLivreur(String nom, String email, String telephone, String cin,
                                Zone zone, Vehicule vehicule, boolean disponible) {
        return livreurRepository.findByEmail(email).orElseGet(() -> {
            Livreur livreur = new Livreur();
            livreur.setNom(nom);
            livreur.setEmail(email);
            livreur.setMotDePasse(passwordEncoder.encode(DEMO_PASSWORD));
            livreur.setTelephone(telephone);
            livreur.setCin(cin);
            livreur.setZone(zone);
            livreur.setVehicule(vehicule);
            livreur.setDisponible(disponible);
            livreur.setRole(role(RoleNom.LIVREUR));
            livreur.setActif(true);
            return livreurRepository.save(livreur);
        });
    }

    private void seedCommande(Client client, Zone zone, String depart, String arrivee,
                              String description, StatutCommande statut, Livreur livreur,
                              Instant assigneeLe) {
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setZone(zone);
        commande.setAdresseDepart(depart);
        commande.setAdresseArrivee(arrivee);
        commande.setDescription(description);
        commande.setTarif(zone.getTarifBase());
        commande.setStatut(statut);
        commande.setLivreur(livreur);
        commande.setCreeLe(Instant.now().minus(2, ChronoUnit.HOURS));

        if (statut == StatutCommande.ASSIGNEE || statut == StatutCommande.EN_COURS || statut == StatutCommande.LIVREE) {
            commande.setAssigneeLe(assigneeLe != null ? assigneeLe : Instant.now().minus(90, ChronoUnit.MINUTES));
        }
        if (statut == StatutCommande.EN_COURS || statut == StatutCommande.LIVREE) {
            commande.setDemarreeLe(Instant.now().minus(30, ChronoUnit.MINUTES));
        }
        if (statut == StatutCommande.LIVREE) {
            commande.setLivreeLe(Instant.now().minus(10, ChronoUnit.MINUTES));
        }
        if (statut == StatutCommande.ANNULEE) {
            commande.setAnnuleeLe(Instant.now().minus(20, ChronoUnit.MINUTES));
        }

        commandeRepository.save(commande);
    }

    private Role role(RoleNom nom) {
        return roleRepository.findByNom(nom)
                .orElseThrow(() -> new IllegalStateException("Rôle manquant: " + nom));
    }
}
