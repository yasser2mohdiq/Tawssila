package com.tawssila.backend.service;

import com.tawssila.backend.dto.*;
import com.tawssila.backend.entity.Client;
import com.tawssila.backend.entity.Livreur;
import com.tawssila.backend.entity.Role;
import com.tawssila.backend.entity.Utilisateur;
import com.tawssila.backend.enums.RoleNom;
import com.tawssila.backend.exception.EmailAlreadyUsedException;
import com.tawssila.backend.exception.InvalidRoleException;
import com.tawssila.backend.exception.ResourceNotFoundException;
import com.tawssila.backend.repository.RoleRepository;
import com.tawssila.backend.repository.UtilisateurRepository;
import com.tawssila.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        ensureEmailAvailable(request.email());

        Role clientRole = findRole(RoleNom.CLIENT);
        Client client = new Client();
        client.setNom(request.nom());
        client.setEmail(request.email());
        client.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        client.setTelephone(request.telephone());
        client.setAdresse(request.adresse());
        client.setRole(clientRole);

        utilisateurRepository.save(client);
        return toAuthResponse(client);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.motDePasse()));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        return toAuthResponse(utilisateur);
    }

    @Transactional(readOnly = true)
    public UserResponse me(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        return toUserResponse(utilisateur);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        RoleNom roleNom = request.role();
        if (roleNom == RoleNom.CLIENT) {
            throw new InvalidRoleException("Utilisez /api/auth/register pour créer un CLIENT");
        }
        if (roleNom != RoleNom.ADMIN && roleNom != RoleNom.DISPATCHER && roleNom != RoleNom.LIVREUR) {
            throw new InvalidRoleException("Rôle non autorisé: " + roleNom);
        }

        ensureEmailAvailable(request.email());
        Role role = findRole(roleNom);

        Utilisateur utilisateur;
        if (roleNom == RoleNom.LIVREUR) {
            Livreur livreur = new Livreur();
            livreur.setDisponible(true);
            utilisateur = livreur;
        } else {
            utilisateur = new Utilisateur();
        }

        utilisateur.setNom(request.nom());
        utilisateur.setEmail(request.email());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        utilisateur.setTelephone(request.telephone());
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);
        return toUserResponse(utilisateur);
    }

    private void ensureEmailAvailable(String email) {
        if (utilisateurRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }
    }

    private Role findRole(RoleNom nom) {
        return roleRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable: " + nom));
    }

    private AuthResponse toAuthResponse(Utilisateur utilisateur) {
        String role = utilisateur.getRole().getNom().name();
        String token = jwtService.generateToken(utilisateur.getEmail(), role);
        return new AuthResponse(token, utilisateur.getEmail(), utilisateur.getNom(), role);
    }

    private UserResponse toUserResponse(Utilisateur utilisateur) {
        return new UserResponse(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getNom(),
                utilisateur.getTelephone(),
                utilisateur.getRole().getNom().name(),
                utilisateur.isActif());
    }
}
