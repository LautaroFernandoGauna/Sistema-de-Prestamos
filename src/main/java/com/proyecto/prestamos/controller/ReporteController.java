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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping ("/reportes")
@Tag(name = "Reportes", description = "Consulta y descarga de reportes de préstamos.")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

     private final ReporteService servicio;

    public ReporteController(ReporteService servicio) { this.servicio = servicio; }

    @GetMapping
            @Operation(summary = "Listar reportes", description = "Devuelve las claves de los reportes disponibles.")
    @ApiResponse(responseCode = "200", description = "Claves de reportes disponibles.")
    public List<String> claves() { return servicio.claves(); }

    @GetMapping("/{clave}")
    @Operation(summary = "Consultar un reporte", description = "Genera el reporte identificado por su clave.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado."),
            @ApiResponse(responseCode = "404", description = "No existe un reporte con esa clave.")
    })
    public Reporte ver(@Parameter(description = "Clave del reporte.", example = "prestamos-activos") @PathVariable String clave) {
        return servicio.generar(clave);
    }

    @GetMapping("/{clave}/csv")
    @Operation(summary = "Descargar un reporte CSV", description = "Descarga el reporte como un archivo CSV codificado en UTF-8.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo CSV generado."),
            @ApiResponse(responseCode = "404", description = "No existe un reporte con esa clave.")
    })
    public ResponseEntity<byte[]> csv(
            @Parameter(description = "Clave del reporte.", example = "prestamos-activos") @PathVariable String clave) {
        byte[] contenido = servicio.csv(clave).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"reporte-" + clave + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(contenido);
    }
}
