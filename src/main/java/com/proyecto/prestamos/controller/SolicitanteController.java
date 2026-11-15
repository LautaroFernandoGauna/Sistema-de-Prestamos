package com.proyecto.prestamos.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.prestamos.dto.AsignarPerfilRequest;
import com.proyecto.prestamos.dto.SolicitanteDTO;
import com.proyecto.prestamos.service.SolicitanteService;

@RestController 
@RequestMapping ("/solicitantes")
public class SolicitanteController {

    private final SolicitanteService servicio;

    public SolicitanteController(SolicitanteService servicio) { this.servicio = servicio; }

    // Cada cuenta ve únicamente su propio perfil.
    @GetMapping("/me")
    public SolicitanteDTO miPerfil(Authentication auth) { return servicio.miPerfil(auth.getName()); }

    // Listado y búsqueda: ADMIN y ENCARGADO (WebSecurityConfig).
    @GetMapping
    public List<SolicitanteDTO> listar() { return servicio.listar(); }

    @GetMapping("/{id}")
    public SolicitanteDTO buscar(@PathVariable Long id) { return servicio.buscar(id); }

    // Solo ADMIN: el solicitante no puede darse a sí mismo un plazo mejor.
    @PutMapping("/{id}/perfil")
    public SolicitanteDTO asignarPerfil(@PathVariable Long id, @RequestBody AsignarPerfilRequest datos) {
        return servicio.asignarPerfil(id, datos == null ? null : datos.perfil());
    }
}
