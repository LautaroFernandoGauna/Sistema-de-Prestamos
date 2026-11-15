package com.proyecto.prestamos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.prestamos.model.Solicitante;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
    Optional<Solicitante> findByUsuarioUsername(String username);
    boolean existsByEmailIgnoreCase(String email);
}
