package com.proyecto.prestamos.template;

import java.util.List;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.Prestamo;
import com.proyecto.prestamos.repository.PrestamoRepository;

@Component 
public class ReporteHistorial extends ReporteTemplate {
    private final PrestamoRepository repo;

    public ReporteHistorial(PrestamoRepository repo) { this.repo = repo; }

    @Override public String clave() { return "historial"; }
    @Override protected String titulo() { return "Historial completo de préstamos"; }
    @Override protected List<Prestamo> obtenerDatos() { return repo.findAllByOrderByFechaPrestamoDesc(); }
}
