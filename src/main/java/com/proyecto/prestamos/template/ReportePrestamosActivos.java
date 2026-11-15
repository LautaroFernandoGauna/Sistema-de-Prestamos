package com.proyecto.prestamos.template;

import java.util.List;

import org.springframework.stereotype.Component;

import com.proyecto.prestamos.model.EstadoPrestamo;
import com.proyecto.prestamos.model.Prestamo;
import com.proyecto.prestamos.repository.PrestamoRepository;

@Component 
public class ReportePrestamosActivos extends ReporteTemplate {
    private final PrestamoRepository repo;

    public ReportePrestamosActivos(PrestamoRepository repo) { this.repo = repo; }

    @Override public String clave() { return "activos"; }
    @Override protected String titulo() { return "Préstamos activos"; }
    @Override protected List<Prestamo> obtenerDatos() {
        return repo.findByEstadoOrderByFechaDevolucionPrevistaAsc(EstadoPrestamo.ACTIVO);
    }
}
