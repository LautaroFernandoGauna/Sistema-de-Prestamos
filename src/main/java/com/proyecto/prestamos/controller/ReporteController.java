package com.proyecto.prestamos.controller;


import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.prestamos.service.ReporteService;
import com.proyecto.prestamos.template.Reporte;

@RestController 
@RequestMapping ("/reportes")
public class ReporteController {

     private final ReporteService servicio;

    public ReporteController(ReporteService servicio) { this.servicio = servicio; }

    @GetMapping
    public List<String> claves() { return servicio.claves(); }

    @GetMapping("/{clave}")
    public Reporte ver(@PathVariable String clave) { return servicio.generar(clave); }

    @GetMapping("/{clave}/csv")
    public ResponseEntity<byte[]> csv(@PathVariable String clave) {
        byte[] contenido = servicio.csv(clave).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"reporte-" + clave + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(contenido);
    }
}
