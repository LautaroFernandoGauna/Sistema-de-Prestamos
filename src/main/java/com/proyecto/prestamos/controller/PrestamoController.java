package com.proyecto.prestamos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.prestamos.dto.PrestamoDTO;
import com.proyecto.prestamos.dto.PrestamoRequest;
import com.proyecto.prestamos.service.PrestamoService;

@RestController 
@RequestMapping ("/prestamos")
public class PrestamoController {
    private final PrestamoService servicio;

    public PrestamoController(PrestamoService servicio) { this.servicio = servicio; }

    // Préstamos activos: ADMIN y ENCARGADO.
    @GetMapping
    public List<PrestamoDTO> activos() { return servicio.activos(); }

    // Cada usuario consulta solo los suyos.
    @GetMapping("/mios")
    public List<PrestamoDTO> mios(Authentication auth) { return servicio.mios(auth.getName()); }

    @PostMapping("/solicitar") @ResponseStatus(HttpStatus.CREATED)
    public PrestamoDTO prestar(@RequestBody PrestamoRequest datos) { return servicio.prestar(datos); }

    // La multa la calcula el servidor según el perfil; el cliente no envía importes.
    @PostMapping("/{id}/devolver")
    public PrestamoDTO devolver(@PathVariable Long id) { return servicio.devolver(id); }
}
