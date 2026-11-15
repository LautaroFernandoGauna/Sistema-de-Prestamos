package com.proyecto.prestamos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.prestamos.dto.EquipoDTO;
import com.proyecto.prestamos.dto.EquipoRequest;
import com.proyecto.prestamos.dto.EstadoEquipoRequest;
import com.proyecto.prestamos.service.EquipoService;

@RestController 
@RequestMapping ("/equipos")
public class EquipoController {

    private final EquipoService servicio;

    public EquipoController(EquipoService servicio) { this.servicio = servicio; }

    // Cualquier usuario autenticado puede consultar.
    @GetMapping
    public List<EquipoDTO> listar(@RequestParam(defaultValue = "false") boolean soloDisponibles) {
        return soloDisponibles ? servicio.disponibles() : servicio.listar();
    }

    @GetMapping("/{id}")
    public EquipoDTO buscar(@PathVariable Long id) { return servicio.buscar(id); }

    // Altas, cambios y bajas solo ADMIN: véase WebSecurityConfig.
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public EquipoDTO crear(@RequestBody EquipoRequest datos) { return servicio.crear(datos); }

    @PutMapping("/{id}/estado")
    public EquipoDTO cambiarEstado(@PathVariable Long id, @RequestBody EstadoEquipoRequest datos) {
        return servicio.cambiarEstado(id, datos == null ? null : datos.estado());
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
