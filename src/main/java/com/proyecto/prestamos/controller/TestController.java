package com.proyecto.prestamos.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Pruebas", description = "Rutas de prueba para comprobar el acceso público y autenticado.")
public class TestController {
    
    @GetMapping("/all")
    @Operation(summary = "Consultar contenido público", description = "Ruta de prueba disponible sin autenticación.")
    @ApiResponse(responseCode = "200", description = "Contenido público.")
    public String allAccess() {
        return "Public-Content.";
    }
    @GetMapping("/user")
    @Operation(summary = "Consultar contenido autenticado", description = "Ruta de prueba disponible con un token JWT válido.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Contenido para usuarios autenticados.")
    public String userAccess() {
        return "User-Content.";
    }
}