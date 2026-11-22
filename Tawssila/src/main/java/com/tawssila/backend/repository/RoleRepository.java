package com.tawssila.backend.repository;

import com.tawssila.backend.entity.Role;
import com.tawssila.backend.enums.RoleNom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(RoleNom nom);
}
