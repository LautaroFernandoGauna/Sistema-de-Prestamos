package com.proyecto.prestamos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.prestamos.model.Equipo;
import com.proyecto.prestamos.model.EstadoEquipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByCodigo(String codigo);
    List<Equipo> findAllByOrderByCodigoAsc();
    List<Equipo> findByEstadoOrderByCodigoAsc(EstadoEquipo estado);
}
