package com.proyecto.prestamos.service;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto.prestamos.dto.EquipoRequest;
import com.proyecto.prestamos.dto.RegistroRequest;
import com.proyecto.prestamos.model.Equipo;
import com.proyecto.prestamos.model.EstadoEquipo;
import com.proyecto.prestamos.model.Perfil;
import com.proyecto.prestamos.model.Prestamo;
import com.proyecto.prestamos.model.Solicitante;
import com.proyecto.prestamos.model.TipoEquipo;
import com.proyecto.prestamos.repository.EquipoRepository;
import com.proyecto.prestamos.repository.PrestamoRepository;
import com.proyecto.prestamos.repository.SolicitanteRepository;

import jakarta.transaction.Transactional;

@Component 
public class DatosDemo implements CommandLineRunner {
    private final EquipoRepository equipoRepo;
    private final EquipoService equipoService;
    private final SolicitanteService solicitanteService;
    private final SolicitanteRepository solicitanteRepo;
    private final PrestamoRepository prestamoRepo;

    public DatosDemo(EquipoRepository equipoRepo, EquipoService equipoService,
                     SolicitanteService solicitanteService, SolicitanteRepository solicitanteRepo,
                     PrestamoRepository prestamoRepo) {
        this.equipoRepo = equipoRepo;
        this.equipoService = equipoService;
        this.solicitanteService = solicitanteService;
        this.solicitanteRepo = solicitanteRepo;
        this.prestamoRepo = prestamoRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (equipoRepo.count() > 0) return;

        solicitanteService.registrar(new RegistroRequest("alumno", "alumno123", "Lucía", "Alumna", "alumno@demo.com"));
        var docente = solicitanteService.registrar(
                new RegistroRequest("docente", "docente123", "Martín", "Docente", "docente@demo.com"));
        solicitanteService.asignarPerfil(docente.idSolicitante(), Perfil.DOCENTE);

        equipoService.crear(new EquipoRequest(TipoEquipo.NOTEBOOK, "NB-001", "Dell", "Latitude 5420", "16 GB RAM"));
        equipoService.crear(new EquipoRequest(TipoEquipo.NOTEBOOK, "NB-002", "Lenovo", "ThinkPad E14", "8 GB RAM"));
        equipoService.crear(new EquipoRequest(TipoEquipo.NOTEBOOK, "NB-003", "HP", "ProBook 450", "8 GB RAM"));
        equipoService.crear(new EquipoRequest(TipoEquipo.PROYECTOR, "PR-001", "Epson", "X49", "3600 lúmenes"));
        equipoService.crear(new EquipoRequest(TipoEquipo.TABLET, "TB-001", "Samsung", "Galaxy Tab A8", "10,5 pulgadas"));

        // Préstamo vencido de ejemplo para probar la multa y el reporte de vencidos.
        Equipo nb3 = equipoRepo.findAllByOrderByCodigoAsc().stream()
                .filter(e -> e.getCodigo().equals("NB-003")).findFirst().orElseThrow();
        Solicitante alumno = solicitanteRepo.findByUsuarioUsername("alumno").orElseThrow();

        Prestamo vencido = new Prestamo();
        vencido.setSolicitante(alumno);
        vencido.setEquipo(nb3);
        vencido.setFechaPrestamo(LocalDate.now().minusDays(8));
        vencido.setFechaDevolucionPrevista(LocalDate.now().minusDays(5));
        nb3.setEstado(EstadoEquipo.PRESTADO);
        equipoRepo.save(nb3);
        prestamoRepo.save(vencido);
    }
}
