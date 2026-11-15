package com.proyecto.prestamos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.proyecto.prestamos.model.EstadoPrestamo;
import com.proyecto.prestamos.model.Prestamo;

@Service
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByEstadoOrderByFechaDevolucionPrevistaAsc(EstadoPrestamo estado);
    List<Prestamo> findBySolicitanteUsuarioUsernameOrderByFechaPrestamoDesc(String username);
    List<Prestamo> findAllByOrderByFechaPrestamoDesc();
}
